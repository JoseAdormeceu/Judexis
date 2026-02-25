package io.judexis.adapter.v1_8.listener;

import io.judexis.adapter.v1_8.bridge.SnapshotBridge;
import io.judexis.adapter.v1_8.session.SessionManager;
import io.judexis.adapter.v1_8.tracker.PingEstimator;
import io.judexis.adapter.v1_8.tracker.TickTask;
import io.judexis.core.JudexisCoreEngine;
import io.judexis.core.domain.PlayerProfile;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Emits combat-related network pulses.
 */
public final class CombatListener implements Listener {
    private final SessionManager sessionManager;
    private final JudexisCoreEngine coreEngine;
    private final SnapshotBridge snapshotBridge;
    private final PingEstimator pingEstimator;
    private final TickTask tickTask;

    public CombatListener(SessionManager sessionManager,
                          JudexisCoreEngine coreEngine,
                          SnapshotBridge snapshotBridge,
                          PingEstimator pingEstimator,
                          TickTask tickTask) {
        this.sessionManager = sessionManager;
        this.coreEngine = coreEngine;
        this.snapshotBridge = snapshotBridge;
        this.pingEstimator = pingEstimator;
        this.tickTask = tickTask;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) {
            return;
        }
        Player attacker = (Player) damager;
        PlayerProfile profile = sessionManager.get(attacker.getUniqueId());
        if (profile == null) {
            return;
        }
        int ping = pingEstimator.estimate(attacker);
        coreEngine.setPingEstimateMillis(profile, ping);
        coreEngine.ingest(profile, snapshotBridge.combatPulse(event, tickTask.getTick(), ping));
    }
}
