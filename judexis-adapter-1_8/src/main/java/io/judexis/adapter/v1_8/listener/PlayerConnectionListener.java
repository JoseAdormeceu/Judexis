package io.judexis.adapter.v1_8.listener;

import io.judexis.adapter.v1_8.bridge.SnapshotBridge;
import io.judexis.adapter.v1_8.session.SessionManager;
import io.judexis.adapter.v1_8.tracker.TickTask;
import io.judexis.core.JudexisCoreEngine;
import io.judexis.core.domain.PlayerProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handles player join and quit session lifecycle.
 */
public final class PlayerConnectionListener implements Listener {
    private final SessionManager sessionManager;
    private final JudexisCoreEngine coreEngine;
    private final SnapshotBridge snapshotBridge;
    private final TickTask tickTask;

    public PlayerConnectionListener(SessionManager sessionManager,
                                    JudexisCoreEngine coreEngine,
                                    SnapshotBridge snapshotBridge,
                                    TickTask tickTask) {
        this.sessionManager = sessionManager;
        this.coreEngine = coreEngine;
        this.snapshotBridge = snapshotBridge;
        this.tickTask = tickTask;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerProfile profile = sessionManager.create(event.getPlayer());
        coreEngine.markJoin(profile);
        coreEngine.ingest(profile, snapshotBridge.worldFromPlayer(event.getPlayer(), tickTask.getTick()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerProfile profile = sessionManager.remove(event.getPlayer().getUniqueId());
        if (profile != null) {
            coreEngine.releasePlayer(profile);
        }
    }
}
