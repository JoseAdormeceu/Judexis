package io.judexis.core.physics.history;

import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.PredictionResult;
import io.judexis.core.snapshot.MovementSnapshot;

/**
 * Immutable history entry for motion analysis.
 */
public final class MotionHistoryEntry {
    private final MovementSnapshot snapshot;
    private final MotionState predictedState;
    private final PredictionResult predictionResult;
    private final long tickIndex;
    private final long timestampNanos;

    public MotionHistoryEntry(MovementSnapshot snapshot, MotionState predictedState,
                              PredictionResult predictionResult, long tickIndex, long timestampNanos) {
        this.snapshot = snapshot;
        this.predictedState = predictedState;
        this.predictionResult = predictionResult;
        this.tickIndex = tickIndex;
        this.timestampNanos = timestampNanos;
    }

    public MovementSnapshot getSnapshot() { return snapshot; }
    public MotionState getPredictedState() { return predictedState; }
    public PredictionResult getPredictionResult() { return predictionResult; }
    public long getTickIndex() { return tickIndex; }
    public long getTimestampNanos() { return timestampNanos; }
}
