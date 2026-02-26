package io.judexis.core.physics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhysicsEngineTest {

    @Test
    void fallingTickProgressionMatchesExpectedVerticalUpdate() {
        PhysicsEngine engine = new PhysicsEngine();
        MotionState state = new MotionState(0.0D, 10.0D, 0.0D, 0.0D, 0.0D, 0.0D,
            false, false, false, false, 0, 0);

        PredictionResult result = engine.simulateNextTick(state, false, 0.0D, 9.9216D, 0.0D, 0.01D);

        assertEquals(-0.0784D, result.getPredictedMotionY(), 1.0E-9D);
        assertEquals(9.9216D, result.getPredictedY(), 1.0E-9D);
        assertTrue(result.isWithinTolerance());
    }

    @Test
    void jumpArcStartsWithExpectedImpulseAndThenDecays() {
        PhysicsEngine engine = new PhysicsEngine();
        MotionState grounded = new MotionState(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D,
            true, false, false, false, 0, 5);

        PredictionResult jumpTick = engine.simulateNextTick(grounded, true, 0.0D, 0.3332D, 0.0D, 0.01D);
        assertEquals(0.3332D, jumpTick.getPredictedMotionY(), 1.0E-9D);
        assertEquals(0.3332D, jumpTick.getPredictedY(), 1.0E-9D);

        PredictionResult nextTick = engine.simulateNextTick(jumpTick.getPredictedState(), false,
            jumpTick.getPredictedX(), jumpTick.getPredictedY() + 0.248136D, jumpTick.getPredictedZ(), 0.01D);
        assertTrue(nextTick.getPredictedMotionY() < jumpTick.getPredictedMotionY());
        assertTrue(nextTick.getPredictedY() > jumpTick.getPredictedY());
    }

    @Test
    void groundFrictionDecaysHorizontalMotion() {
        PhysicsEngine engine = new PhysicsEngine();
        MotionState grounded = new MotionState(0.0D, 0.0D, 0.0D, 0.5D, 0.0D, -0.5D,
            true, false, false, false, 0, 1);

        PredictionResult result = engine.simulateNextTick(grounded, false, 0.273D, 0.0D, -0.273D, 0.01D);

        assertEquals(0.273D, result.getPredictedMotionX(), 1.0E-9D);
        assertEquals(-0.273D, result.getPredictedMotionZ(), 1.0E-9D);
        assertTrue(result.isWithinTolerance());
    }
}
