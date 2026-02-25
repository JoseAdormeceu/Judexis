package io.judexis.core.check;

import io.judexis.core.context.ContextState;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.ViolationAccumulator;

/**
 * Context passed to checks on each dispatch.
 */
public final class CheckContext {
    private final PlayerProfile profile;
    private final ContextState contextState;
    private final ViolationAccumulator violationAccumulator;

    public CheckContext(PlayerProfile profile, ContextState contextState, ViolationAccumulator violationAccumulator) {
        this.profile = profile;
        this.contextState = contextState;
        this.violationAccumulator = violationAccumulator;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public ContextState getContextState() {
        return contextState;
    }

    public ViolationAccumulator getViolationAccumulator() {
        return violationAccumulator;
    }

    public void emit(Evidence evidence) {
        violationAccumulator.append(evidence);
    }
}
