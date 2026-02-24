package judexis.core.enforcement;

import judexis.JudexisPlugin;
import judexis.core.audit.AuditLayer;
import judexis.core.enforcement.EnforcementAction;
import judexis.core.enforcement.EnforcementEngine;
import judexis.core.evidence.EvidenceEngine;
import judexis.core.evidence.Phase;
import judexis.core.player.PlayerSnapshot;
import judexis.core.profile.DetectionProfile;
import judexis.core.profile.ProfileEngine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class DefaultEnforcementManager implements EnforcementEngine {

    private final JudexisPlugin plugin;
    private final ProfileEngine profileEngine;
    private final EvidenceEngine evidenceEngine;
    private final AuditLayer auditLayer;
    private final Set<UUID> frozenPlayers;
    private volatile boolean enforcementEnabled;

    public DefaultEnforcementManager(JudexisPlugin plugin,
                                     ProfileEngine profileEngine,
                                     EvidenceEngine evidenceEngine,
                                     AuditLayer auditLayer) {
        this.plugin = plugin;
        this.profileEngine = profileEngine;
        this.evidenceEngine = evidenceEngine;
        this.auditLayer = auditLayer;
        this.frozenPlayers = new HashSet<UUID>();
        this.enforcementEnabled = true;
    }

    @Override
    public void evaluate(PlayerSnapshot snapshot, Phase phase, DetectionProfile profile, long tick) {
        if (!enforcementEnabled) {
            return;
        }
        if (profileEngine.panicMode() || profileEngine.maintenanceMode()) {
            return;
        }
        if (!profile.enforcementEnabled()) {
            return;
        }
        if (phase != Phase.ENFORCEMENT_AUTHORIZED) {
            return;
        }

        UUID playerId = snapshot.getPlayerId();
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            return;
        }

        if (profile.freezeEnabled()) {
            freeze(playerId, "system");
            evidenceEngine.recordEnforcement(snapshot, EnforcementAction.FREEZE, tick);
        }
    }

    @Override
    public void freeze(UUID playerId, String actorName) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            return;
        }
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.setWalkSpeed(0.0F);
            frozenPlayers.add(playerId);
            auditLayer.log("ENFORCE_FREEZE", playerId, actorName, true);
        });
    }

    @Override
    public void unfreeze(UUID playerId, String actorName) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            return;
        }
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.setWalkSpeed(0.2F);
            frozenPlayers.remove(playerId);
            auditLayer.log("ENFORCE_UNFREEZE", playerId, actorName, true);
        });
    }

    @Override
    public void setEnforcementEnabled(boolean enabled) {
        this.enforcementEnabled = enabled;
    }

    @Override
    public boolean isEnforcementEnabled() {
        return enforcementEnabled;
    }
}
