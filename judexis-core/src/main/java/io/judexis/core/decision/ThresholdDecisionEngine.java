package io.judexis.core.decision;

import io.judexis.core.context.ContextState;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.violation.ViolationAccumulator;

/**
 * Baseline decision engine using numeric thresholds only.
 */
public final class ThresholdDecisionEngine implements DecisionEngine {
    private final double reviewThreshold;
    private final double actionableThreshold;

    public ThresholdDecisionEngine(double reviewThreshold, double actionableThreshold) {
        if (reviewThreshold < 0.0D || actionableThreshold < reviewThreshold) {
            throw new IllegalArgumentException("thresholds are invalid");
        }
        this.reviewThreshold = reviewThreshold;
        this.actionableThreshold = actionableThreshold;
    }

    public Decision evaluate(PlayerProfile profile, ContextState context, ViolationAccumulator accumulator) {
        double score = accumulator.getTotalViolationScore();
        if (score >= actionableThreshold) {
            return new Decision(DecisionOutcome.ACTIONABLE, score, "score-above-actionable-threshold");
        }
        if (score >= reviewThreshold) {
            return new Decision(DecisionOutcome.REVIEW, score, "score-above-review-threshold");
        }
        return Decision.none();
    }
}
