package io.judexis.adapter.v1_8.tracker;

import io.judexis.adapter.v1_8.bridge.SnapshotBridge;
import io.judexis.adapter.v1_8.session.SessionManager;
import io.judexis.core.JudexisCoreEngine;
import io.judexis.core.domain.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Per-tick task responsible for context updates and heartbeat snapshots.
 */
public final class TickTask extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final SessionManager sessionManager;
    private final JudexisCoreEngine coreEngine;
    private final SnapshotBridge snapshotBridge;
    private final TpsTracker tpsTracker;
    private final PingEstimator pingEstimator;
    private long tick;

    public TickTask(JavaPlugin plugin,
                    SessionManager sessionManager,
                    JudexisCoreEngine coreEngine,
                    SnapshotBridge snapshotBridge,
                    TpsTracker tpsTracker,
                    PingEstimator pingEstimator) {
        this.plugin = plugin;
        this.sessionManager = sessionManager;
        this.coreEngine = coreEngine;
        this.snapshotBridge = snapshotBridge;
        this.tpsTracker = tpsTracker;
        this.pingEstimator = pingEstimator;
    }

    /**
     * Starts this task with one-tick period.
     */
    public void start() {
        runTaskTimer(plugin, 1L, 1L);
    }

    public void run() {
        tick++;
        double tps = tpsTracker.recordTickAndEstimate();
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            PlayerProfile profile = sessionManager.get(player.getUniqueId());
            if (profile == null) {
                continue;
            }
            int ping = pingEstimator.estimate(player);
            coreEngine.setTicksPerSecond(profile, tps);
            coreEngine.setPingEstimateMillis(profile, ping);
            coreEngine.ingest(profile, snapshotBridge.networkHeartbeat(tick, ping));
        }
    }

    public long getTick() {
        return tick;
    }
}
