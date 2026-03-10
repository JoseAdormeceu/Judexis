package io.judexis.core.check.movement;

import io.judexis.core.physics.MotionState;

/**
 * Shared movement prediction context consumed by movement checks.
 */
public final class MovementPredictionContext {
    private final MotionState current;
    private final MotionState previous;
    private final double observedDeltaX;
    private final double observedDeltaY;
    private final double observedDeltaZ;
    private final boolean recentVelocity;
    private final boolean recentTeleport;
    private final CheckConfidence confidence;

    public MovementPredictionContext(MotionState current, MotionState previous,
                                     double observedDeltaX, double observedDeltaY, double observedDeltaZ,
                                     boolean recentVelocity, boolean recentTeleport,
                                     CheckConfidence confidence) {
        this.current = current;
        this.previous = previous;
        this.observedDeltaX = observedDeltaX;
        this.observedDeltaY = observedDeltaY;
        this.observedDeltaZ = observedDeltaZ;
        this.recentVelocity = recentVelocity;
        this.recentTeleport = recentTeleport;
        this.confidence = confidence;
    }

    public MotionState getCurrent() { return current; }
    public MotionState getPrevious() { return previous; }
    public double getObservedDeltaX() { return observedDeltaX; }
    public double getObservedDeltaY() { return observedDeltaY; }
    public double getObservedDeltaZ() { return observedDeltaZ; }
    public boolean isRecentVelocity() { return recentVelocity; }
    public boolean isRecentTeleport() { return recentTeleport; }
    public CheckConfidence getConfidence() { return confidence; }
}
