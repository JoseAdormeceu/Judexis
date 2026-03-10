package io.judexis.core.context;

import io.judexis.core.violation.ViolationAccumulator;

/**
 * Runtime context associated with one player identity.
 */
public final class PlayerRuntimeContext {
    private final ContextState state;
    private final ViolationAccumulator violationAccumulator;
    private String currentWorldId;

    public PlayerRuntimeContext(int evidenceCapacity) {
        this.state = new ContextState();
        this.violationAccumulator = new ViolationAccumulator(evidenceCapacity);
    }

    public ContextState getState() {
        return state;
    }

    public ViolationAccumulator getViolationAccumulator() {
        return violationAccumulator;
    }

    public String getCurrentWorldId() {
        return currentWorldId;
    }

    public void setCurrentWorldId(String currentWorldId) {
        this.currentWorldId = currentWorldId;
    }
}
