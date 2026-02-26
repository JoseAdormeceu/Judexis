package io.judexis.core.physics;

/**
 * Deterministic 1.8-like physics approximation for one-tick movement prediction.
 */
public final class PhysicsEngine {
    public static final double GRAVITY = 0.08D;
    public static final double DRAG = 0.98D;
    public static final double GROUND_FRICTION = 0.6D;
    public static final double JUMP_VELOCITY = 0.42D;
    private static final double AIR_HORIZONTAL_DRAG = 0.91D;
    private static final double ICE_FRICTION = 0.98D;
    private static final double SLIME_FRICTION = 0.8D;

    public double applyGravity(double motionY, boolean onGround, boolean inLiquid) {
        if (onGround || inLiquid) {
            return motionY;
        }
        return motionY - GRAVITY;
    }

    public double applyFriction(double horizontalMotion, boolean onGround, boolean onIce, boolean onSlime) {
        if (!onGround) {
            return horizontalMotion * AIR_HORIZONTAL_DRAG;
        }
        double base = GROUND_FRICTION;
        if (onIce) {
            base = ICE_FRICTION;
        } else if (onSlime) {
            base = SLIME_FRICTION;
        }
        return horizontalMotion * (base * AIR_HORIZONTAL_DRAG);
    }

    public double applyJump(boolean onGround, boolean jumpRequested, double currentMotionY) {
        if (onGround && jumpRequested) {
            return JUMP_VELOCITY;
        }
        return currentMotionY;
    }

    public double applyAirDrag(double motionAxis, boolean inLiquid) {
        if (inLiquid) {
            return motionAxis * 0.8D;
        }
        return motionAxis * DRAG;
    }

    public PredictionResult simulateNextTick(MotionState current) {
        return simulateNextTick(current, false, current.getX(), current.getY(), current.getZ(), 0.03D);
    }

    public PredictionResult simulateNextTick(MotionState current, boolean jumpRequested,
                                             double realX, double realY, double realZ,
                                             double toleranceMargin) {
        double motionX = current.getMotionX();
        double motionY = current.getMotionY();
        double motionZ = current.getMotionZ();
        boolean onGround = current.isOnGround();

        motionY = applyJump(onGround, jumpRequested, motionY);
        if (jumpRequested && onGround) {
            onGround = false;
        }

        motionY = applyGravity(motionY, onGround, current.isInLiquid());
        motionY = applyAirDrag(motionY, current.isInLiquid());

        motionX = applyFriction(motionX, onGround, current.isOnIce(), current.isOnSlime());
        motionZ = applyFriction(motionZ, onGround, current.isOnIce(), current.isOnSlime());

        double nextX = current.getX() + motionX;
        double nextY = current.getY() + motionY;
        double nextZ = current.getZ() + motionZ;

        boolean nextGround = onGround;
        if (nextY <= 0.0D) {
            nextY = 0.0D;
            motionY = 0.0D;
            nextGround = true;
        }

        int airTicks = nextGround ? 0 : current.getAirTicks() + 1;
        int groundTicks = nextGround ? current.getGroundTicks() + 1 : 0;

        MotionState predicted = new MotionState(
            nextX, nextY, nextZ,
            motionX, motionY, motionZ,
            nextGround, current.isInLiquid(), current.isOnIce(), current.isOnSlime(),
            airTicks, groundTicks
        );

        double dx = realX - nextX;
        double dy = realY - nextY;
        double dz = realZ - nextZ;
        double error = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));

        return new PredictionResult(predicted, motionX, motionY, motionZ, nextX, nextY, nextZ, error, toleranceMargin);
    }
}
