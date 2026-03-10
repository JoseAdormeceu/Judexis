package io.judexis.core.data;

import io.judexis.core.domain.PlayerProfile;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Thread-safe storage for per-player runtime data.
 */
public final class PlayerDataStore {
    private final ConcurrentMap<UUID, PlayerData> playerData;
    private final int evidenceCapacity;

    public PlayerDataStore(int evidenceCapacity) {
        if (evidenceCapacity <= 0) {
            throw new IllegalArgumentException("evidenceCapacity must be positive");
        }
        this.playerData = new ConcurrentHashMap<UUID, PlayerData>();
        this.evidenceCapacity = evidenceCapacity;
    }

    public PlayerData getOrCreate(PlayerProfile profile) {
        return playerData.computeIfAbsent(profile.getUniqueId(), id -> new PlayerData(evidenceCapacity));
    }

    public PlayerData get(PlayerProfile profile) {
        return playerData.get(profile.getUniqueId());
    }

    public void remove(PlayerProfile profile) {
        playerData.remove(profile.getUniqueId());
    }
}
