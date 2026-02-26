package io.judexis.core.context;

/**
 * Mutable tick-based context that allows checks to reason about state transitions.
 */
public final class ContextState {
    private int teleportTicks;
    private int velocityTicks;
    private int joinTicks;
    private int worldChangeTicks;
    private double ticksPerSecond;
    private int pingEstimateMillis;

    public ContextState() {
        this.teleportTicks = Integer.MAX_VALUE;
        this.velocityTicks = Integer.MAX_VALUE;
        this.joinTicks = 0;
        this.worldChangeTicks = Integer.MAX_VALUE;
        this.ticksPerSecond = 20.0D;
        this.pingEstimateMillis = 0;
    }

    /**
     * Advances all tick counters by one server tick.
     */
    public void advanceTick() {
        teleportTicks = safeIncrement(teleportTicks);
        velocityTicks = safeIncrement(velocityTicks);
        joinTicks = safeIncrement(joinTicks);
        worldChangeTicks = safeIncrement(worldChangeTicks);
    }

    public void markTeleport() {
        teleportTicks = 0;
    }

    public void markVelocity() {
        velocityTicks = 0;
    }

    public void markJoin() {
        joinTicks = 0;
    }

    public void markWorldChange() {
        worldChangeTicks = 0;
    }

    public void setTicksPerSecond(double ticksPerSecond) {
        if (ticksPerSecond <= 0.0D) {
            throw new IllegalArgumentException("ticksPerSecond must be positive");
        }
        this.ticksPerSecond = ticksPerSecond;
    }

    public void setPingEstimateMillis(int pingEstimateMillis) {
        if (pingEstimateMillis < 0) {
            throw new IllegalArgumentException("pingEstimateMillis cannot be negative");
        }
        this.pingEstimateMillis = pingEstimateMillis;
    }

    public int getTeleportTicks() {
        return teleportTicks;
    }

    public int getVelocityTicks() {
        return velocityTicks;
    }

    public int getJoinTicks() {
        return joinTicks;
    }

    public int getWorldChangeTicks() {
        return worldChangeTicks;
    }

    public double getTicksPerSecond() {
        return ticksPerSecond;
    }

    public int getPingEstimateMillis() {
        return pingEstimateMillis;
    }

    private int safeIncrement(int value) {
        if (value == Integer.MAX_VALUE) {
            return value;
        }
        return value + 1;
    }
}
