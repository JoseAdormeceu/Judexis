package io.judexis.core.context;

import io.judexis.core.domain.PlayerProfile;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Concurrent repository for per-player runtime context.
 */
public final class ContextRegistry {
    private final ConcurrentMap<UUID, PlayerRuntimeContext> contexts;
    private final int evidenceCapacityPerPlayer;

    public ContextRegistry(int evidenceCapacityPerPlayer) {
        if (evidenceCapacityPerPlayer <= 0) {
            throw new IllegalArgumentException("evidenceCapacityPerPlayer must be positive");
        }
        this.contexts = new ConcurrentHashMap<UUID, PlayerRuntimeContext>();
        this.evidenceCapacityPerPlayer = evidenceCapacityPerPlayer;
    }

    public PlayerRuntimeContext getOrCreate(PlayerProfile profile) {
        return contexts.computeIfAbsent(profile.getUniqueId(),
            unused -> new PlayerRuntimeContext(evidenceCapacityPerPlayer));
    }

    public void remove(PlayerProfile profile) {
        contexts.remove(profile.getUniqueId());
    }
}
