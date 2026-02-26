package io.judexis.core.decision;

import io.judexis.core.context.ContextState;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.violation.ViolationAccumulator;

/**
 * Strategy interface that translates accumulated evidence into policy-neutral decisions.
 */
public interface DecisionEngine {
    /**
     * Evaluates the current player state and violation accumulator.
     *
     * @param profile immutable player identity
     * @param context mutable runtime context
     * @param accumulator per-player evidence accumulator
     * @return decision projection
     */
    Decision evaluate(PlayerProfile profile, ContextState context, ViolationAccumulator accumulator);
}
