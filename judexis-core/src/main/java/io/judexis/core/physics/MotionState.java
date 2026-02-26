package io.judexis.core.physics;

/**
 * Immutable player motion state used by the deterministic physics simulation.
 */
public final class MotionState {
    private final double x;
    private final double y;
    private final double z;
    private final double motionX;
    private final double motionY;
    private final double motionZ;
    private final boolean onGround;
    private final boolean inLiquid;
    private final boolean onIce;
    private final boolean onSlime;
    private final int airTicks;
    private final int groundTicks;

    public MotionState(double x, double y, double z,
                       double motionX, double motionY, double motionZ,
                       boolean onGround, boolean inLiquid, boolean onIce, boolean onSlime,
                       int airTicks, int groundTicks) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.onGround = onGround;
        this.inLiquid = inLiquid;
        this.onIce = onIce;
        this.onSlime = onSlime;
        this.airTicks = airTicks;
        this.groundTicks = groundTicks;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getMotionX() { return motionX; }
    public double getMotionY() { return motionY; }
    public double getMotionZ() { return motionZ; }
    public boolean isOnGround() { return onGround; }
    public boolean isInLiquid() { return inLiquid; }
    public boolean isOnIce() { return onIce; }
    public boolean isOnSlime() { return onSlime; }
    public int getAirTicks() { return airTicks; }
    public int getGroundTicks() { return groundTicks; }
}
