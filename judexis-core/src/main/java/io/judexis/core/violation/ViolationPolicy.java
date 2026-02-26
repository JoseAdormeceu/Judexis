package io.judexis.core.violation;

import io.judexis.core.decision.DecisionEvent;
import io.judexis.core.decision.DecisionEventType;
import io.judexis.core.domain.PlayerProfile;

import java.util.Map;

/**
 * Computes rolling per-check and global scores from evidence.
 */
public final class ViolationPolicy {
    private final double decay;
    private final double checkAlertThreshold;
    private final double globalAlertThreshold;

    public ViolationPolicy(double decay, double checkAlertThreshold, double globalAlertThreshold) {
        if (decay < 0.0D || decay > 1.0D) {
            throw new IllegalArgumentException("decay must be in [0,1]");
        }
        this.decay = decay;
        this.checkAlertThreshold = checkAlertThreshold;
        this.globalAlertThreshold = globalAlertThreshold;
    }

    public DecisionEvent evaluate(PlayerProfile profile, String checkId, Evidence evidence, ViolationPolicyState state) {
        double weight = evidence.getSeverity().getScoreWeight();

        double nextGlobal = (state.getGlobalRollingScore() * decay) + weight;
        double nextCheck = (state.getCheckRollingScore(checkId) * decay) + weight;

        Map<EvidenceType, Double> totals = state.snapshotCategoryTotals();
        Double currentCategory = totals.get(evidence.getType());
        double nextCategory = (currentCategory == null ? 0.0D : currentCategory.doubleValue()) + weight;
        state.update(nextGlobal, checkId, nextCheck, evidence.getType(), nextCategory);

        if (nextCheck >= checkAlertThreshold) {
            return new DecisionEvent(DecisionEventType.ALERT, profile, checkId, nextCheck,
                nextGlobal, System.nanoTime(), "check-threshold");
        }
        if (nextGlobal >= globalAlertThreshold) {
            return new DecisionEvent(DecisionEventType.ALERT, profile, checkId, nextCheck,
                nextGlobal, System.nanoTime(), "global-threshold");
        }
        return null;
    }
}
