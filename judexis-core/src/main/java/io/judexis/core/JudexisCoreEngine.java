package io.judexis.core;

import io.judexis.core.check.Check;
import io.judexis.core.context.ContextRegistry;
import io.judexis.core.decision.Decision;
import io.judexis.core.decision.DecisionEngine;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.pipeline.DefaultInputBus;
import io.judexis.core.pipeline.InputBus;
import io.judexis.core.snapshot.Snapshot;

/**
 * Core composition root that wires state registry, input bus, and decision engine.
 */
public final class JudexisCoreEngine {
    private final InputBus inputBus;

    public JudexisCoreEngine(DecisionEngine decisionEngine, int evidenceCapacityPerPlayer) {
        this.inputBus = new DefaultInputBus(new ContextRegistry(evidenceCapacityPerPlayer), decisionEngine);
    }

    public void registerCheck(Check check) {
        inputBus.register(check);
    }

    public void unregisterCheck(Check check) {
        inputBus.unregister(check);
    }

    public Decision ingest(PlayerProfile profile, Snapshot snapshot) {
        return inputBus.publish(profile, snapshot);
    }

    public void releasePlayer(PlayerProfile profile) {
        inputBus.release(profile);
    }
}
