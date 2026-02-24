package judexis.core.behavior;

import com.comphenix.protocol.events.PacketEvent;
import judexis.core.config.ConfigurationService;
import judexis.core.context.ContextReliabilityEngine;
import judexis.core.player.PlayerDataSnapshotLayer;
import judexis.core.player.PlayerSnapshot;
import judexis.core.player.runtime.PlayerData;
import judexis.core.profile.ProfileEngine;
import judexis.core.suspicion.SuspicionEngine;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BehavioralDetectionManager implements BehavioralDetectionLayer {

    private final ContextReliabilityEngine contextEngine;
    private final ConfigurationService configurationService;
    private final SuspicionEngine suspicionEngine;
    private final ProfileEngine profileEngine;
    private final Map<UUID, PlayerModuleRuntime> runtimes;
    private long currentTick;

    public BehavioralDetectionManager(ContextReliabilityEngine contextEngine,
                                      ConfigurationService configurationService,
                                      SuspicionEngine suspicionEngine,
                                      ProfileEngine profileEngine) {
        this.contextEngine = contextEngine;
        this.configurationService = configurationService;
        this.suspicionEngine = suspicionEngine;
        this.profileEngine = profileEngine;
        this.runtimes = new ConcurrentHashMap<UUID, PlayerModuleRuntime>();
        this.currentTick = 0L;
    }

    @Override
    public void handle(PacketEvent event, PlayerDataSnapshotLayer snapshotLayer) {
        UUID playerId = event.getPlayer().getUniqueId();
        PlayerSnapshot snapshot = snapshotLayer.getSnapshot(playerId);
        if (!(snapshot instanceof PlayerData)) {
            return;
        }

        PlayerData data = (PlayerData) snapshot;
        double reliability = contextEngine.reliability(data, System.currentTimeMillis());
        PlayerModuleRuntime runtime = runtimes.computeIfAbsent(playerId, ignored -> new PlayerModuleRuntime());
        runtime.handle(event, data, currentTick, reliability, suspicionEngine);
    }

    @Override
    public void tick(long currentTick) {
        this.currentTick = currentTick;
        double profileMultiplier = Math.max(0.65D, profileEngine.activeProfile().suspicionMultiplier());
        double decay = configurationService.baseDecayPerTick() / profileMultiplier;
        for (PlayerModuleRuntime runtime : runtimes.values()) {
            runtime.decay(decay);
        }
    }
}
