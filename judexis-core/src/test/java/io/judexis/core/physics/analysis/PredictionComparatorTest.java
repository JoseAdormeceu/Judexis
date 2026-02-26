package io.judexis.core.physics.analysis;

import io.judexis.core.context.ContextState;
import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.PredictionResult;
import io.judexis.core.snapshot.MovementSnapshot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PredictionComparatorTest {

    @Test
    void toleranceIncreasesWithPing() {
        PredictionComparator comparator = new PredictionComparator();

        MovementSnapshot snapshot = new MovementSnapshot(1L, 100L, 1.03D, 64.0D, 2.0D, 1.0D, 64.0D, 2.0D, 0.0F, 0.0F, true);
        MotionState predictedState = new MotionState(1.0D, 64.0D, 2.0D, 0.03D, 0.0D, 0.0D, true, false, false, false, 0, 1);
        PredictionResult prediction = new PredictionResult(predictedState, 0.03D, 0.0D, 0.0D, 1.0D, 64.0D, 2.0D, 0.0D, 0.03D);

        ContextState lowPing = new ContextState();
        lowPing.setPingEstimateMillis(20);
        lowPing.setTicksPerSecond(20.0D);

        ContextState highPing = new ContextState();
        highPing.setPingEstimateMillis(220);
        highPing.setTicksPerSecond(20.0D);

        ErrorMeasurement low = comparator.computeError(snapshot, prediction, lowPing);
        ErrorMeasurement high = comparator.computeError(snapshot, prediction, highPing);

        assertTrue(high.getTolerance() > low.getTolerance());
    }
}
