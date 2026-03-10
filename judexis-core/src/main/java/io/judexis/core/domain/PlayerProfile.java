package io.judexis.core.domain;

import java.util.UUID;

/**
 * Immutable identity model for a tracked player.
 */
public final class PlayerProfile {
    private final UUID uniqueId;
    private final String username;
    private final long joinedAtNanos;

    /**
     * Creates a new profile.
     *
     * @param uniqueId unique player id
     * @param username player name
     * @param joinedAtNanos monotonic join time in nanoseconds
     */
    public PlayerProfile(UUID uniqueId, String username, long joinedAtNanos) {
        if (uniqueId == null) {
            throw new IllegalArgumentException("uniqueId is required");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("username is required");
        }
        this.uniqueId = uniqueId;
        this.username = username;
        this.joinedAtNanos = joinedAtNanos;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getUsername() {
        return username;
    }

    public long getJoinedAtNanos() {
        return joinedAtNanos;
    }
}
