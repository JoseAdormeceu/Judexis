package io.judexis.core.violation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Mutable rolling scores maintained by violation policy.
 */
public final class ViolationPolicyState {
    private final Map<String, Double> checkRollingScore;
    private final EnumMap<EvidenceType, Double> categoryTotals;
    private double globalRollingScore;

    public ViolationPolicyState() {
        this.checkRollingScore = new HashMap<String, Double>();
        this.categoryTotals = new EnumMap<EvidenceType, Double>(EvidenceType.class);
        for (EvidenceType type : EvidenceType.values()) {
            categoryTotals.put(type, Double.valueOf(0.0D));
        }
    }

    public synchronized double getGlobalRollingScore() {
        return globalRollingScore;
    }

    public synchronized double getCheckRollingScore(String checkId) {
        Double value = checkRollingScore.get(checkId);
        return value == null ? 0.0D : value.doubleValue();
    }

    public synchronized Map<EvidenceType, Double> snapshotCategoryTotals() {
        return new EnumMap<EvidenceType, Double>(categoryTotals);
    }

    synchronized void update(double globalScore, String checkId, double checkScore, EvidenceType type, double categoryTotal) {
        this.globalRollingScore = globalScore;
        checkRollingScore.put(checkId, Double.valueOf(checkScore));
        categoryTotals.put(type, Double.valueOf(categoryTotal));
    }
}
