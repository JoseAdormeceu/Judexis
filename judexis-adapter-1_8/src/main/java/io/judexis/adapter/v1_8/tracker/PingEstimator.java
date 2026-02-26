package io.judexis.adapter.v1_8.tracker;

import org.bukkit.entity.Player;

/**
 * Resolves ping estimates from Bukkit/Spigot API.
 */
public final class PingEstimator {

    /**
     * Estimates ping for a player.
     *
     * @param player online player
     * @return ping in milliseconds, or zero when not available
     */
    public int estimate(Player player) {
        int ping = player.spigot().getPing();
        if (ping < 0) {
            return 0;
        }
        return ping;
    }
}
