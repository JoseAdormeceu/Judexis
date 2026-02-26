package io.judexis.core.physics.history;

import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.PredictionResult;
import io.judexis.core.snapshot.MovementSnapshot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MotionHistoryBufferTest {

    @Test
    void overwritesOldestEntriesOnOverflow() {
        MotionHistoryBuffer buffer = new MotionHistoryBuffer(3);

        addEntry(buffer, 1L);
        addEntry(buffer, 2L);
        addEntry(buffer, 3L);
        addEntry(buffer, 4L);

        assertEquals(3, buffer.size());
        assertTrue(buffer.isFull());
        assertEquals(4L, buffer.getRelative(0).getTickIndex());
        assertEquals(3L, buffer.getRelative(1).getTickIndex());
        assertEquals(2L, buffer.getRelative(2).getTickIndex());
    }

    private void addEntry(MotionHistoryBuffer buffer, long tick) {
        MovementSnapshot snapshot = new MovementSnapshot(tick, tick * 10L, tick, tick, tick, tick - 1, tick - 1, tick - 1, 0.0F, 0.0F, false);
        MotionState state = new MotionState(tick, tick, tick, 0.1D, 0.1D, 0.1D, false, false, false, false, 1, 0);
        PredictionResult result = new PredictionResult(state, 0.1D, 0.1D, 0.1D, tick, tick, tick, 0.0D, 0.03D);
        buffer.add(snapshot, state, result);
    }
}
