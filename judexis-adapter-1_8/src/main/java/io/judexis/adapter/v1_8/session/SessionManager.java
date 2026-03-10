package io.judexis.adapter.v1_8.session;

import io.judexis.core.domain.PlayerProfile;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks active player profiles mapped by UUID.
 */
public final class SessionManager {
    private final Map<UUID, PlayerProfile> profiles = new ConcurrentHashMap<UUID, PlayerProfile>();

    /**
     * Creates and stores a profile for an online player.
     *
     * @param player online player
     * @return created profile
     */
    public PlayerProfile create(Player player) {
        PlayerProfile profile = new PlayerProfile(player.getUniqueId(), player.getName(), System.nanoTime());
        profiles.put(profile.getUniqueId(), profile);
        return profile;
    }

    /**
     * Finds an existing profile.
     *
     * @param uniqueId player UUID
     * @return profile or null
     */
    public PlayerProfile get(UUID uniqueId) {
        return profiles.get(uniqueId);
    }

    /**
     * Removes and returns profile.
     *
     * @param uniqueId player UUID
     * @return removed profile or null
     */
    public PlayerProfile remove(UUID uniqueId) {
        return profiles.remove(uniqueId);
    }

    /**
     * @return read-only view of active profiles
     */
    public Collection<PlayerProfile> all() {
        return Collections.unmodifiableCollection(profiles.values());
    }
}
