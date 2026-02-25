package io.judexis.core.debug;

import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.EvidenceType;

import java.util.List;
import java.util.Map;

/**
 * Read-only debug projection for one player.
 */
public final class CoreDebugSnapshot {
    private final int pingEstimateMillis;
    private final double ticksPerSecond;
    private final int joinTicks;
    private final int teleportTicks;
    private final int velocityTicks;
    private final Map<EvidenceType, Double> violationByCategory;
    private final List<Evidence> recentEvidence;

    public CoreDebugSnapshot(int pingEstimateMillis, double ticksPerSecond, int joinTicks,
                             int teleportTicks, int velocityTicks,
                             Map<EvidenceType, Double> violationByCategory,
                             List<Evidence> recentEvidence) {
        this.pingEstimateMillis = pingEstimateMillis;
        this.ticksPerSecond = ticksPerSecond;
        this.joinTicks = joinTicks;
        this.teleportTicks = teleportTicks;
        this.velocityTicks = velocityTicks;
        this.violationByCategory = violationByCategory;
        this.recentEvidence = recentEvidence;
    }

    public int getPingEstimateMillis() {
        return pingEstimateMillis;
    }

    public double getTicksPerSecond() {
        return ticksPerSecond;
    }

    public int getJoinTicks() {
        return joinTicks;
    }

    public int getTeleportTicks() {
        return teleportTicks;
    }

    public int getVelocityTicks() {
        return velocityTicks;
    }

    public Map<EvidenceType, Double> getViolationByCategory() {
        return violationByCategory;
    }

    public List<Evidence> getRecentEvidence() {
        return recentEvidence;
    }
}
