package io.judexis.core.physics.history;

import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.PredictionResult;
import io.judexis.core.physics.analysis.ErrorMeasurement;
import io.judexis.core.snapshot.MovementSnapshot;

/**
 * Fixed-size circular buffer for movement prediction history.
 */
public final class MotionHistoryBuffer {
    public static final int DEFAULT_CAPACITY = 30;

    private final MotionHistoryEntry[] entries;
    private int head;
    private int size;

    public MotionHistoryBuffer() {
        this(DEFAULT_CAPACITY);
    }

    public MotionHistoryBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.entries = new MotionHistoryEntry[capacity];
        this.head = -1;
        this.size = 0;
    }

    public void add(MovementSnapshot snapshot, MotionState predictedState, PredictionResult predictionResult) {
        add(snapshot, predictedState, predictionResult, null);
    }

    public void add(MovementSnapshot snapshot, MotionState predictedState,
                    PredictionResult predictionResult, ErrorMeasurement errorMeasurement) {
        if (snapshot == null || predictedState == null || predictionResult == null) {
            throw new IllegalArgumentException("snapshot, predictedState and predictionResult are required");
        }
        head++;
        if (head >= entries.length) {
            head = 0;
        }
        entries[head] = new MotionHistoryEntry(
            snapshot,
            predictedState,
            predictionResult,
            errorMeasurement,
            snapshot.getTick(),
            snapshot.getCapturedAtNanos()
        );
        if (size < entries.length) {
            size++;
        }
    }

    public MotionHistoryEntry getRelative(int ticksAgo) {
        if (ticksAgo < 0 || ticksAgo >= size) {
            return null;
        }
        int index = head - ticksAgo;
        if (index < 0) {
            index += entries.length;
        }
        return entries[index];
    }

    public int size() { return size; }

    public boolean isFull() { return size == entries.length; }
}
