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
<<<<<<< codex/generate-structure-for-judexis-anti-cheat-system-xj4ljw
    private final boolean nearGround;
    private final boolean inLiquid;
    private final boolean onIce;
    private final boolean onSlime;
    private final boolean touchingBlockAbove;
    private final double blockFriction;

    public MovementSnapshot(long tick, long capturedAtNanos, double x, double y, double z,
                            double previousX, double previousY, double previousZ,
                            float yaw, float pitch,
                            boolean onGround,
                            boolean nearGround,
                            boolean inLiquid,
                            boolean onIce,
                            boolean onSlime,
                            boolean touchingBlockAbove,
                            double blockFriction) {
=======

    public MovementSnapshot(long tick, long capturedAtNanos, double x, double y, double z,
                            double previousX, double previousY, double previousZ,
                            float yaw, float pitch, boolean onGround) {
>>>>>>> main
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
<<<<<<< codex/generate-structure-for-judexis-anti-cheat-system-xj4ljw
        this.nearGround = nearGround;
        this.inLiquid = inLiquid;
        this.onIce = onIce;
        this.onSlime = onSlime;
        this.touchingBlockAbove = touchingBlockAbove;
        this.blockFriction = blockFriction;
=======
>>>>>>> main
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

<<<<<<< codex/generate-structure-for-judexis-anti-cheat-system-xj4ljw
    public boolean isNearGround() {
        return nearGround;
    }

    public boolean isInLiquid() {
        return inLiquid;
    }

    public boolean isOnIce() {
        return onIce;
    }

    public boolean isOnSlime() {
        return onSlime;
    }

    public boolean isTouchingBlockAbove() {
        return touchingBlockAbove;
    }

    public double getBlockFriction() {
        return blockFriction;
    }

=======
>>>>>>> main
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
