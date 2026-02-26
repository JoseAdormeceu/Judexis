package io.judexis.adapter.v1_8;

import io.judexis.adapter.v1_8.bridge.SnapshotBridge;
import io.judexis.adapter.v1_8.command.JudexisCommand;
import io.judexis.adapter.v1_8.listener.CombatListener;
import io.judexis.adapter.v1_8.listener.MovementListener;
import io.judexis.adapter.v1_8.listener.PlayerConnectionListener;
import io.judexis.adapter.v1_8.session.SessionManager;
import io.judexis.adapter.v1_8.tracker.PingEstimator;
import io.judexis.adapter.v1_8.tracker.TickTask;
import io.judexis.adapter.v1_8.tracker.TpsTracker;
import io.judexis.core.JudexisCoreEngine;
import io.judexis.core.check.Check;
import io.judexis.core.check.CheckCategory;
import io.judexis.core.check.CheckContext;
import io.judexis.core.decision.ThresholdDecisionEngine;
import io.judexis.core.snapshot.Snapshot;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
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
        this.coreEngine = new JudexisCoreEngine(new ThresholdDecisionEngine(25.0D, 50.0D), 128);
        this.snapshotBridge = new SnapshotBridge();
        this.pingEstimator = new PingEstimator();
        this.tickTask = new TickTask(this, sessionManager, coreEngine, snapshotBridge, new TpsTracker(100), pingEstimator);

        registerBuiltinChecks();
        registerListeners(getServer().getPluginManager());
        registerCommands();

        coreEngine.subscribeDecisionEvents(event -> getLogger().info(
            "Decision " + event.getType().name() + " player=" + event.getProfile().getUsername()
                + " check=" + event.getCheckId()
                + " checkScore=" + event.getCheckScore()
                + " globalScore=" + event.getGlobalScore()
                + " reason=" + event.getReason()
        ));

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
            coreEngine.stop();
        }

        getLogger().info("Judexis adapter 1_8 disabled.");
    }

    private void registerBuiltinChecks() {
        coreEngine.registerCheck("debug.passive", CheckCategory.MISC, new Check() {
            public void onLoad() {
            }

            public void onSnapshot(CheckContext context, Snapshot snapshot) {
            }

            public void onUnload() {
            }

            public String getName() {
                return "debug.passive";
            }
        });
    }

    private void registerCommands() {
        PluginCommand command = getCommand("judexis");
        if (command != null) {
            command.setExecutor(new JudexisCommand(coreEngine, sessionManager));
        }
    }

    private void registerListeners(PluginManager pluginManager) {
        pluginManager.registerEvents(new PlayerConnectionListener(sessionManager, coreEngine, snapshotBridge, tickTask), this);
        pluginManager.registerEvents(new MovementListener(sessionManager, coreEngine, snapshotBridge, tickTask), this);
        pluginManager.registerEvents(new CombatListener(sessionManager, coreEngine, snapshotBridge, pingEstimator, tickTask), this);
    }
}
