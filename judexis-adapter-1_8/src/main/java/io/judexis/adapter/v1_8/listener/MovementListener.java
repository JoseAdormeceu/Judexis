package io.judexis.adapter.v1_8.listener;

import io.judexis.adapter.v1_8.bridge.SnapshotBridge;
import io.judexis.adapter.v1_8.session.SessionManager;
import io.judexis.adapter.v1_8.tracker.TickTask;
import io.judexis.core.JudexisCoreEngine;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.snapshot.MovementSnapshot;
import io.judexis.core.snapshot.WorldSnapshot;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Emits movement and world snapshots from movement-related events.
 */
public final class MovementListener implements Listener {
    private final SessionManager sessionManager;
    private final JudexisCoreEngine coreEngine;
    private final SnapshotBridge snapshotBridge;
    private final TickTask tickTask;

    public MovementListener(SessionManager sessionManager,
                            JudexisCoreEngine coreEngine,
                            SnapshotBridge snapshotBridge,
                            TickTask tickTask) {
        this.sessionManager = sessionManager;
        this.coreEngine = coreEngine;
        this.snapshotBridge = snapshotBridge;
        this.tickTask = tickTask;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!hasPositionChanged(event.getFrom(), event.getTo())) {
            return;
        }
        PlayerProfile profile = sessionManager.get(event.getPlayer().getUniqueId());
        if (profile == null) {
            return;
        }
        long tick = tickTask.getTick();
        MovementSnapshot movementSnapshot = snapshotBridge.movement(event, tick);
        WorldSnapshot worldSnapshot = snapshotBridge.world(event.getTo(), tick, false);
        coreEngine.ingest(profile, movementSnapshot);
        coreEngine.ingest(profile, worldSnapshot);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        PlayerProfile profile = sessionManager.get(event.getPlayer().getUniqueId());
        if (profile != null) {
            coreEngine.markTeleport(profile);
        }
    }

    private boolean hasPositionChanged(Location from, Location to) {
        if (to == null) {
            return false;
        }
        return from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
    }
}
