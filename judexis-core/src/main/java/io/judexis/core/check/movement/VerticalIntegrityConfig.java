package io.judexis.core.check.movement;

/**
 * Runtime configuration for vertical integrity analysis.
 */
public final class VerticalIntegrityConfig {
    private final int minHistory;
    private final int patternWindow;
    private final int minMismatches;
    private final int maxPing;
    private final double minTps;
    private final int joinGraceTicks;
    private final int teleportGraceTicks;
    private final int velocityGraceTicks;
    private final int worldChangeGraceTicks;
    private final double verticalScoreThreshold;
    private final double normalizedErrorThreshold;
    private final double decaySensitivity;

    public VerticalIntegrityConfig() {
        this(10, 10, 6, 250, 18.0D, 40, 5, 10, 20, 0.45D, 1.05D, 1.0D);
    }

    public VerticalIntegrityConfig(int minHistory, int patternWindow, int minMismatches, int maxPing,
                                   double minTps, int joinGraceTicks, int teleportGraceTicks,
                                   int velocityGraceTicks, int worldChangeGraceTicks,
                                   double verticalScoreThreshold, double normalizedErrorThreshold,
                                   double decaySensitivity) {
        this.minHistory = minHistory;
        this.patternWindow = patternWindow;
        this.minMismatches = minMismatches;
        this.maxPing = maxPing;
        this.minTps = minTps;
        this.joinGraceTicks = joinGraceTicks;
        this.teleportGraceTicks = teleportGraceTicks;
        this.velocityGraceTicks = velocityGraceTicks;
        this.worldChangeGraceTicks = worldChangeGraceTicks;
        this.verticalScoreThreshold = verticalScoreThreshold;
        this.normalizedErrorThreshold = normalizedErrorThreshold;
        this.decaySensitivity = decaySensitivity;
    }

    public int getMinHistory() { return minHistory; }
    public int getPatternWindow() { return patternWindow; }
    public int getMinMismatches() { return minMismatches; }
    public int getMaxPing() { return maxPing; }
    public double getMinTps() { return minTps; }
    public int getJoinGraceTicks() { return joinGraceTicks; }
    public int getTeleportGraceTicks() { return teleportGraceTicks; }
    public int getVelocityGraceTicks() { return velocityGraceTicks; }
    public int getWorldChangeGraceTicks() { return worldChangeGraceTicks; }
    public double getVerticalScoreThreshold() { return verticalScoreThreshold; }
    public double getNormalizedErrorThreshold() { return normalizedErrorThreshold; }
    public double getDecaySensitivity() { return decaySensitivity; }
}
