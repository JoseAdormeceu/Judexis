package io.judexis.core.violation;

import io.judexis.core.data.PlayerData;
import io.judexis.core.decision.DecisionEvent;
import io.judexis.core.decision.DecisionEventBus;
import io.judexis.core.domain.PlayerProfile;

/**
 * Routes check evidence into accumulators and policy evaluation.
 */
public final class EvidenceRouter {
    private final ViolationPolicy policy;
    private final DecisionEventBus eventBus;

    public EvidenceRouter(ViolationPolicy policy, DecisionEventBus eventBus) {
        this.policy = policy;
        this.eventBus = eventBus;
    }

    public void route(PlayerProfile profile, String checkId, Evidence evidence, PlayerData data) {
        data.getAccumulator().append(evidence);
        DecisionEvent event = policy.evaluate(profile, checkId, evidence, data.getPolicyState());
        if (event != null) {
            eventBus.publish(event);
        }
    }
}
