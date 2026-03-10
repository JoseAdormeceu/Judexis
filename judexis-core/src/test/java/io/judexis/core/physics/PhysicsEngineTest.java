package io.judexis.core.physics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhysicsEngineTest {

    @Test
    void fallingTickProgressionMatchesExpectedVerticalUpdate() {
        PhysicsEngine engine = new PhysicsEngine();
        MotionState state = new MotionState(0.0D, 10.0D, 0.0D, 0.0D, 0.0D, 0.0D,
            false, false, false, false, false, false, 0.6D, 0, 0);

        PredictionResult result = engine.simulateNextTick(state, false, 0.0D, 9.9216D, 0.0D, 0.01D);

        assertEquals(-0.0784D, result.getPredictedMotionY(), 1.0E-9D);
        assertEquals(9.9216D, result.getPredictedY(), 1.0E-9D);
        assertTrue(result.isWithinTolerance());
    }

    @Test
    void jumpArcStartsWithExpectedImpulseAndThenDecays() {
        PhysicsEngine engine = new PhysicsEngine();
        MotionState grounded = new MotionState(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D,
            true, false, false, false, true, false, 0.6D, 0, 5);

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
            true, false, false, false, true, false, 0.6D, 0, 1);

        PredictionResult result = engine.simulateNextTick(grounded, false, 0.273D, 0.0D, -0.273D, 0.01D);

        assertEquals(0.273D, result.getPredictedMotionX(), 1.0E-9D);
        assertEquals(-0.273D, result.getPredictedMotionZ(), 1.0E-9D);
        assertTrue(result.isWithinTolerance());
    }

    @Test
    void respectsSuppliedSurfaceFriction() {
        PhysicsEngine engine = new PhysicsEngine();
        MotionState normal = new MotionState(0.0D, 64.0D, 0.0D, 0.4D, 0.0D, 0.0D,
            true, false, false, false, true, false, 0.6D, 0, 1);
        MotionState slick = new MotionState(0.0D, 64.0D, 0.0D, 0.4D, 0.0D, 0.0D,
            true, false, false, false, true, false, 0.98D, 0, 1);

        PredictionResult normalResult = engine.simulateNextTick(normal);
        PredictionResult slickResult = engine.simulateNextTick(slick);

        assertTrue(slickResult.getPredictedMotionX() > normalResult.getPredictedMotionX());
    }

    @Test
    void nearGroundCountsAsGroundForFriction() {
        PhysicsEngine engine = new PhysicsEngine();
        MotionState nearGround = new MotionState(0.0D, 64.1D, 0.0D, 0.3D, -0.02D, 0.0D,
            false, false, false, false, true, false, 0.6D, 1, 0);
        MotionState air = new MotionState(0.0D, 64.1D, 0.0D, 0.3D, -0.02D, 0.0D,
            false, false, false, false, false, false, 0.6D, 1, 0);

        PredictionResult nearGroundResult = engine.simulateNextTick(nearGround);
        PredictionResult airResult = engine.simulateNextTick(air);

        assertTrue(nearGroundResult.getPredictedMotionX() < airResult.getPredictedMotionX());
    }

    @Test
    void ceilingContactClampsUpwardMotion() {
        PhysicsEngine engine = new PhysicsEngine();
        MotionState state = new MotionState(0.0D, 70.0D, 0.0D, 0.0D, 0.2D, 0.0D,
            false, false, false, false, false, true, 0.91D, 3, 0);

        PredictionResult result = engine.simulateNextTick(state);
        assertTrue(result.getPredictedMotionY() < 0.0D);
    }
}
