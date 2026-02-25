package io.judexis.core.data;

import io.judexis.core.context.ContextState;
import io.judexis.core.violation.ViolationAccumulator;
import io.judexis.core.violation.ViolationPolicyState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-player mutable anti-cheat runtime data.
 */
public final class PlayerData {
    private final ContextState contextState;
    private final ViolationAccumulator accumulator;
    private final ViolationPolicyState policyState;
    private final Map<String, Object> checkState;
    private String currentWorldId;

    public PlayerData(int evidenceCapacity) {
        this.contextState = new ContextState();
        this.accumulator = new ViolationAccumulator(evidenceCapacity);
        this.policyState = new ViolationPolicyState();
        this.checkState = new ConcurrentHashMap<String, Object>();
    }

    public ContextState getContextState() {
        return contextState;
    }

    public ViolationAccumulator getAccumulator() {
        return accumulator;
    }

    public ViolationPolicyState getPolicyState() {
        return policyState;
    }

    public Object getCheckState(String key) {
        return checkState.get(key);
    }

    public String getCurrentWorldId() {
        return currentWorldId;
    }

    public void setCurrentWorldId(String currentWorldId) {
        this.currentWorldId = currentWorldId;
    }

    public void setCheckState(String key, Object value) {
        if (value == null) {
            checkState.remove(key);
            return;
        }
        checkState.put(key, value);
    }
}
