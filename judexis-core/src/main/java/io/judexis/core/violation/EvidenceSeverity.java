package io.judexis.core.violation;

/**
 * Severity with fixed score contribution.
 */
public enum EvidenceSeverity {
    LOW(1.0D),
    MEDIUM(2.5D),
    HIGH(5.0D),
    CRITICAL(10.0D);

    private final double scoreWeight;

    EvidenceSeverity(double scoreWeight) {
        this.scoreWeight = scoreWeight;
    }

    public double getScoreWeight() {
        return scoreWeight;
    }
}
