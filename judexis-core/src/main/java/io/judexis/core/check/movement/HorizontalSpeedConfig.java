package io.judexis.core.check.movement;

/**
 * Runtime configuration for horizontal speed analysis.
 */
public final class HorizontalSpeedConfig {
    private final int minHistory;
    private final int maxPing;
    private final double idealTps;
    private final int joinGraceTicks;
    private final int teleportGraceTicks;
    private final int velocityGraceTicks;
    private final int worldChangeGraceTicks;
    private final double baseAllowance;

    public HorizontalSpeedConfig() {
        this(3, 250, 20.0D, 40, 5, 10, 20, 0.03D);
    }

    public HorizontalSpeedConfig(int minHistory, int maxPing, double idealTps,
                                 int joinGraceTicks, int teleportGraceTicks,
                                 int velocityGraceTicks, int worldChangeGraceTicks,
                                 double baseAllowance) {
        this.minHistory = minHistory;
        this.maxPing = maxPing;
        this.idealTps = idealTps;
        this.joinGraceTicks = joinGraceTicks;
        this.teleportGraceTicks = teleportGraceTicks;
        this.velocityGraceTicks = velocityGraceTicks;
        this.worldChangeGraceTicks = worldChangeGraceTicks;
        this.baseAllowance = baseAllowance;
    }

    public int getMinHistory() { return minHistory; }
    public int getMaxPing() { return maxPing; }
    public double getIdealTps() { return idealTps; }
    public int getJoinGraceTicks() { return joinGraceTicks; }
    public int getTeleportGraceTicks() { return teleportGraceTicks; }
    public int getVelocityGraceTicks() { return velocityGraceTicks; }
    public int getWorldChangeGraceTicks() { return worldChangeGraceTicks; }
    public double getBaseAllowance() { return baseAllowance; }
}
