package judexis.core;

import judexis.JudexisPlugin;
import judexis.core.audit.AuditLayer;
import judexis.core.behavior.BehavioralDetectionLayer;
import judexis.core.command.CommandSystem;
import judexis.core.config.ConfigurationService;
import judexis.core.context.ContextReliabilityEngine;
import judexis.core.enforcement.EnforcementEngine;
import judexis.core.evidence.EvidenceEngine;
import judexis.core.gui.GuiControlSuite;
import judexis.core.packet.PacketCaptureLayer;
import judexis.core.player.PlayerDataSnapshotLayer;
import judexis.core.player.runtime.PlayerData;
import judexis.core.player.runtime.PlayerDataRepository;
import judexis.core.profile.ProfileEngine;
import judexis.core.suspicion.DefaultSuspicionEngine;
import judexis.core.suspicion.SuspicionEngine;
import judexis.infrastructure.audit.FileAuditLogger;
import judexis.core.behavior.BehavioralDetectionManager;
import judexis.infrastructure.command.JudexisCommandSystem;
import judexis.infrastructure.config.BukkitConfigurationService;
import judexis.core.context.DefaultContextReliabilityEngine;
import judexis.core.enforcement.DefaultEnforcementManager;
import judexis.core.evidence.EvidenceManager;
import judexis.infrastructure.gui.GuiController;
import judexis.infrastructure.packet.ProtocolLibPacketCapture;
import judexis.core.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;

public final class JudexisCore {

    private final JudexisPlugin plugin;

    private final ConfigurationService configurationService;
    private final AuditLayer auditLayer;
    private final PlayerDataSnapshotLayer playerDataLayer;
    private final ProfileEngine profileEngine;
    private final ContextReliabilityEngine contextEngine;
    private final EvidenceEngine evidenceEngine;
    private final SuspicionEngine suspicionEngine;
    private final EnforcementEngine enforcementEngine;
    private final BehavioralDetectionLayer behavioralLayer;
    private final GuiControlSuite guiSuite;
    private final CommandSystem commandSystem;
    private final PacketCaptureLayer packetCaptureLayer;

    private BukkitTask tickTask;
    private long tickCounter;

    public JudexisCore(JudexisPlugin plugin) {
        this.plugin = plugin;
        this.configurationService = new BukkitConfigurationService(plugin);
        this.auditLayer = new FileAuditLogger(plugin);
        this.playerDataLayer = new PlayerDataRepository(configurationService);
        this.profileEngine = new ProfileManager(configurationService, auditLayer);
        this.contextEngine = new DefaultContextReliabilityEngine(configurationService);
        this.evidenceEngine = new EvidenceManager();
        this.suspicionEngine = new DefaultSuspicionEngine(evidenceEngine);
        this.enforcementEngine = new DefaultEnforcementManager(plugin, profileEngine, evidenceEngine, auditLayer);
        this.behavioralLayer = new BehavioralDetectionManager(contextEngine, configurationService, suspicionEngine, profileEngine);
        this.guiSuite = new GuiController(plugin, profileEngine, auditLayer);
        this.commandSystem = new JudexisCommandSystem(plugin, profileEngine, guiSuite, configurationService, enforcementEngine, auditLayer);
        this.packetCaptureLayer = new ProtocolLibPacketCapture(plugin, playerDataLayer, behavioralLayer);
    }

    public void start() {
        this.packetCaptureLayer.start();
        this.commandSystem.register();
        this.tickTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            tickCounter++;
            this.behavioralLayer.tick(tickCounter);
            Collection<judexis.core.player.PlayerSnapshot> snapshots = this.playerDataLayer.allSnapshots();
            for (judexis.core.player.PlayerSnapshot snapshot : snapshots) {
                if (snapshot instanceof PlayerData) {
                    PlayerData data = (PlayerData) snapshot;
                    Player player = Bukkit.getPlayer(data.getPlayerId());
                    if (player != null) {
                        data.setTpsSnapshot(20.0D);
                    }
                }
                this.enforcementEngine.evaluate(
                        snapshot,
                        this.evidenceEngine.currentPhase(snapshot),
                        this.profileEngine.activeProfile(),
                        tickCounter
                );
            }
        }, 1L, 1L);
    }

    public void stop() {
        if (this.tickTask != null) {
            this.tickTask.cancel();
        }
        this.packetCaptureLayer.stop();
        this.auditLayer.close();
    }
}