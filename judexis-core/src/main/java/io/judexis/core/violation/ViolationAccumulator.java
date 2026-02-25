package io.judexis.core.violation;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Per-player evidence accumulator with bounded retention for recent evidence details.
 */
public final class ViolationAccumulator {
    private final int evidenceCapacity;
    private final ArrayDeque<Evidence> recentEvidence;
    private double totalViolationScore;
    private long evidenceCount;

    public ViolationAccumulator(int evidenceCapacity) {
        if (evidenceCapacity <= 0) {
            throw new IllegalArgumentException("evidenceCapacity must be positive");
        }
        this.evidenceCapacity = evidenceCapacity;
        this.recentEvidence = new ArrayDeque<Evidence>(evidenceCapacity);
    }

    public synchronized void append(Evidence evidence) {
        if (evidence == null) {
            throw new IllegalArgumentException("evidence is required");
        }
        if (recentEvidence.size() == evidenceCapacity) {
            recentEvidence.removeFirst();
        }
        recentEvidence.addLast(evidence);
        totalViolationScore += evidence.getSeverity().getScoreWeight();
        evidenceCount++;
    }

    public synchronized double getTotalViolationScore() {
        return totalViolationScore;
    }

    public synchronized long getEvidenceCount() {
        return evidenceCount;
    }

    public synchronized List<Evidence> snapshotRecentEvidence() {
        return new ArrayList<Evidence>(recentEvidence);
    }
}
