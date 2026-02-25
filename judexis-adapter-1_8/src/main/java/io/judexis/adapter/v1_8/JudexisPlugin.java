package io.judexis.adapter.v1_8;

import io.judexis.adapter.v1_8.bridge.SnapshotBridge;
import io.judexis.adapter.v1_8.listener.CombatListener;
import io.judexis.adapter.v1_8.listener.MovementListener;
import io.judexis.adapter.v1_8.listener.PlayerConnectionListener;
import io.judexis.adapter.v1_8.session.SessionManager;
import io.judexis.adapter.v1_8.tracker.PingEstimator;
import io.judexis.adapter.v1_8.tracker.TickTask;
import io.judexis.adapter.v1_8.tracker.TpsTracker;
import io.judexis.core.JudexisCoreEngine;
import io.judexis.core.decision.ThresholdDecisionEngine;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Bukkit/Paper 1.8 adapter bootstrap for Judexis core.
 */
public final class JudexisPlugin extends JavaPlugin {
    private SessionManager sessionManager;
    private JudexisCoreEngine coreEngine;
    private SnapshotBridge snapshotBridge;
    private PingEstimator pingEstimator;
    private TickTask tickTask;

    public void onEnable() {
        this.sessionManager = new SessionManager();
        this.coreEngine = new JudexisCoreEngine(new ThresholdDecisionEngine(25.0D, 50.0D), 64);
        this.snapshotBridge = new SnapshotBridge();
        this.pingEstimator = new PingEstimator();
        this.tickTask = new TickTask(this, sessionManager, coreEngine, snapshotBridge, new TpsTracker(100), pingEstimator);

        registerListeners(getServer().getPluginManager());
        this.tickTask.start();

        getLogger().info("Judexis adapter 1_8 enabled.");
    }

    public void onDisable() {
        if (tickTask != null) {
            tickTask.cancel();
        }

        if (coreEngine != null && sessionManager != null) {
            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                io.judexis.core.domain.PlayerProfile profile = sessionManager.remove(player.getUniqueId());
                if (profile != null) {
                    coreEngine.releasePlayer(profile);
                }
            }
        }

        getLogger().info("Judexis adapter 1_8 disabled.");
    }

    private void registerListeners(PluginManager pluginManager) {
        pluginManager.registerEvents(
            new PlayerConnectionListener(sessionManager, coreEngine, snapshotBridge, tickTask),
            this
        );
        pluginManager.registerEvents(
            new MovementListener(sessionManager, coreEngine, snapshotBridge, tickTask),
            this
        );
        pluginManager.registerEvents(
            new CombatListener(sessionManager, coreEngine, snapshotBridge, pingEstimator, tickTask),
            this
        );
    }
}
