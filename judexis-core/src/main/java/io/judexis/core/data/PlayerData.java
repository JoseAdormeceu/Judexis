package io.judexis.core.data;

import io.judexis.core.context.ContextState;
import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.PredictionResult;
import io.judexis.core.physics.analysis.ErrorAccumulator;
import io.judexis.core.physics.history.MotionHistoryBuffer;
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
    private final MotionHistoryBuffer motionHistoryBuffer;
    private final ErrorAccumulator errorAccumulator;

    private String currentWorldId;
    private MotionState lastObservedMotionState;
    private MotionState pendingPredictedState;
    private PredictionResult pendingPredictionResult;

    public PlayerData(int evidenceCapacity) {
        this.contextState = new ContextState();
        this.accumulator = new ViolationAccumulator(evidenceCapacity);
        this.policyState = new ViolationPolicyState();
        this.checkState = new ConcurrentHashMap<String, Object>();
        this.motionHistoryBuffer = new MotionHistoryBuffer();
        this.errorAccumulator = new ErrorAccumulator();
    }

    public ContextState getContextState() { return contextState; }
    public ViolationAccumulator getAccumulator() { return accumulator; }
    public ViolationPolicyState getPolicyState() { return policyState; }
    public MotionHistoryBuffer getMotionHistoryBuffer() { return motionHistoryBuffer; }
    public ErrorAccumulator getErrorAccumulator() { return errorAccumulator; }

    public Object getCheckState(String key) { return checkState.get(key); }

    public String getCurrentWorldId() { return currentWorldId; }
    public void setCurrentWorldId(String currentWorldId) { this.currentWorldId = currentWorldId; }

    public MotionState getLastObservedMotionState() { return lastObservedMotionState; }
    public void setLastObservedMotionState(MotionState lastObservedMotionState) { this.lastObservedMotionState = lastObservedMotionState; }

    public MotionState getPendingPredictedState() { return pendingPredictedState; }
    public void setPendingPredictedState(MotionState pendingPredictedState) { this.pendingPredictedState = pendingPredictedState; }

    public PredictionResult getPendingPredictionResult() { return pendingPredictionResult; }
    public void setPendingPredictionResult(PredictionResult pendingPredictionResult) { this.pendingPredictionResult = pendingPredictionResult; }

    public void setCheckState(String key, Object value) {
        if (value == null) {
            checkState.remove(key);
            return;
        }
        checkState.put(key, value);
    }
}
