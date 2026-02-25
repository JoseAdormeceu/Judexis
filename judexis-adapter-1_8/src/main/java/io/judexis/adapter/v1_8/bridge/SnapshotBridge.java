package io.judexis.adapter.v1_8.bridge;

import io.judexis.core.snapshot.MovementSnapshot;
import io.judexis.core.snapshot.NetworkSnapshot;
import io.judexis.core.snapshot.WorldMedium;
import io.judexis.core.snapshot.WorldSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Maps Bukkit events and entities into core snapshot models.
 */
public final class SnapshotBridge {

    /**
     * Converts move events into movement snapshots.
     *
     * @param event move event
     * @param tick current server tick
     * @return movement snapshot
     */
    public MovementSnapshot movement(PlayerMoveEvent event, long tick) {
        Location from = event.getFrom();
        Location to = event.getTo() == null ? from : event.getTo();
        return new MovementSnapshot(
            tick,
            System.nanoTime(),
            to.getX(),
            to.getY(),
            to.getZ(),
            from.getX(),
            from.getY(),
            from.getZ(),
            to.getYaw(),
            to.getPitch(),
            event.getPlayer().isOnGround()
        );
    }

    /**
     * Converts a location into world snapshot.
     *
     * @param location source location
     * @param tick current tick
     * @param collidedHorizontally collision estimate
     * @return world snapshot
     */
    public WorldSnapshot world(Location location, long tick, boolean collidedHorizontally) {
        Block block = location.getBlock();
        return new WorldSnapshot(
            tick,
            System.nanoTime(),
            location.getWorld().getName(),
            block.getX(),
            block.getY(),
            block.getZ(),
            resolveMedium(block.getType()),
            collidedHorizontally
        );
    }

    /**
     * Converts a heartbeat for network conditions.
     *
     * @param tick current tick
     * @param ping ping estimate in milliseconds
     * @return network snapshot
     */
    public NetworkSnapshot networkHeartbeat(long tick, int ping) {
        return new NetworkSnapshot(tick, System.nanoTime(), ping, 20, 0, 0);
    }

    /**
     * Converts combat events into a network snapshot pulse.
     *
     * @param event combat event
     * @param tick current tick
     * @param ping ping estimate
     * @return network snapshot
     */
    public NetworkSnapshot combatPulse(EntityDamageByEntityEvent event, long tick, int ping) {
        int pressure = Math.max(1, event.getDamage() > 0.0D ? 24 : 20);
        return new NetworkSnapshot(tick, System.nanoTime(), ping, pressure, 0, 0);
    }

    /**
     * @param player player
     * @return current location world snapshot
     */
    public WorldSnapshot worldFromPlayer(Player player, long tick) {
        return world(player.getLocation(), tick, false);
    }

    private WorldMedium resolveMedium(Material material) {
        if (material == Material.WATER || material == Material.STATIONARY_WATER
            || material == Material.LAVA || material == Material.STATIONARY_LAVA) {
            return WorldMedium.LIQUID;
        }
        if (material == Material.AIR) {
            return WorldMedium.AIR;
        }
        return WorldMedium.SOLID;
    }
}
