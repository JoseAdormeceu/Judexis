package io.judexis.core.physics.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorAccumulatorTest {

    @Test
    void appliesDecayExactlyOncePerMeasurement() {
        ErrorAccumulator accumulator = new ErrorAccumulator(5, 0.95D);
        accumulator.addMeasurement(new ErrorMeasurement(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 2.0D, 1.0D, 0.1D));
        accumulator.addMeasurement(new ErrorMeasurement(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 2.0D, 1.0D, 0.1D));

        assertTrue(accumulator.getCurrentScore() > 2.0D);
        assertTrue(accumulator.getCurrentScore() < 4.0D);
    }

    @Test
    void detectsThresholdExceeded() {
        ErrorAccumulator accumulator = new ErrorAccumulator(5, 1.0D);
        accumulator.addMeasurement(new ErrorMeasurement(0.0D, 0.0D, 0.0D, 2.0D, 2.0D, 4.0D, 1.0D, 0.1D));
        assertTrue(accumulator.exceedsThreshold(3.5D));
        assertFalse(accumulator.exceedsThreshold(5.0D));
    }
}
