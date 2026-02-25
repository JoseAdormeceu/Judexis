package io.judexis.core.snapshot;

/**
 * Positional and look snapshot captured each movement update.
 */
public final class MovementSnapshot implements Snapshot {
    private final long tick;
    private final long capturedAtNanos;
    private final double x;
    private final double y;
    private final double z;
    private final double previousX;
    private final double previousY;
    private final double previousZ;
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public MovementSnapshot(long tick, long capturedAtNanos, double x, double y, double z,
                            double previousX, double previousY, double previousZ,
                            float yaw, float pitch, boolean onGround) {
        this.tick = tick;
        this.capturedAtNanos = capturedAtNanos;
        this.x = x;
        this.y = y;
        this.z = z;
        this.previousX = previousX;
        this.previousY = previousY;
        this.previousZ = previousZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public long getTick() {
        return tick;
    }

    public long getCapturedAtNanos() {
        return capturedAtNanos;
    }

    public SnapshotType getType() {
        return SnapshotType.MOVEMENT;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getPreviousX() {
        return previousX;
    }

    public double getPreviousY() {
        return previousY;
    }

    public double getPreviousZ() {
        return previousZ;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public double deltaX() {
        return x - previousX;
    }

    public double deltaY() {
        return y - previousY;
    }

    public double deltaZ() {
        return z - previousZ;
    }

    public double horizontalDistanceSquared() {
        double dx = deltaX();
        double dz = deltaZ();
        return dx * dx + dz * dz;
    }
}
