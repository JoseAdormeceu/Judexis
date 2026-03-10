package io.judexis.core.decision;

import io.judexis.core.domain.PlayerProfile;

/**
 * Immutable decision event emitted by policy threshold triggers.
 */
public final class DecisionEvent {
    private final DecisionEventType type;
    private final PlayerProfile profile;
    private final String checkId;
    private final double checkScore;
    private final double globalScore;
    private final long timestampNanos;
    private final String reason;

    public DecisionEvent(DecisionEventType type, PlayerProfile profile, String checkId,
                         double checkScore, double globalScore, long timestampNanos, String reason) {
        this.type = type;
        this.profile = profile;
        this.checkId = checkId;
        this.checkScore = checkScore;
        this.globalScore = globalScore;
        this.timestampNanos = timestampNanos;
        this.reason = reason;
    }

    public DecisionEventType getType() {
        return type;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public String getCheckId() {
        return checkId;
    }

    public double getCheckScore() {
        return checkScore;
    }

    public double getGlobalScore() {
        return globalScore;
    }

    public long getTimestampNanos() {
        return timestampNanos;
    }

    public String getReason() {
        return reason;
    }
}
