package io.judexis.core.pipeline;

import io.judexis.core.check.Check;
import io.judexis.core.check.CheckContext;
import io.judexis.core.context.ContextRegistry;
import io.judexis.core.decision.Decision;
import io.judexis.core.decision.DecisionEngine;
import io.judexis.core.decision.DecisionOutcome;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.snapshot.NetworkSnapshot;
import io.judexis.core.snapshot.Snapshot;
import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.EvidenceSeverity;
import io.judexis.core.violation.EvidenceType;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultInputBusTest {

    @Test
    void dispatchesSnapshotAndReturnsDecision() {
        DecisionEngine decisionEngine = (profile, context, accumulator) -> {
            if (accumulator.getTotalViolationScore() > 0.0D) {
                return new Decision(DecisionOutcome.REVIEW, accumulator.getTotalViolationScore(), "test");
            }
            return Decision.none();
        };

        DefaultInputBus inputBus = new DefaultInputBus(new ContextRegistry(8), decisionEngine);
        RecordingCheck check = new RecordingCheck();
        inputBus.register(check);

        PlayerProfile profile = new PlayerProfile(UUID.randomUUID(), "Architect", 10L);
        Decision decision = inputBus.publish(profile, new NetworkSnapshot(1L, 20L, 150, 40, 2, 1));

        assertEquals(DecisionOutcome.REVIEW, decision.getOutcome());
        assertTrue(check.loaded);
        assertEquals(1, check.snapshotCalls);
        assertEquals(150, check.latestContext.getContextState().getPingEstimateMillis());

        inputBus.unregister(check);
        assertTrue(check.unloaded);
    }

    private static final class RecordingCheck implements Check {
        private boolean loaded;
        private boolean unloaded;
        private int snapshotCalls;
        private CheckContext latestContext;

        public void onLoad() {
            loaded = true;
        }

        public void onSnapshot(CheckContext context, Snapshot snapshot) {
            latestContext = context;
            snapshotCalls++;
            context.emit(new Evidence(getName(), EvidenceType.NETWORK, EvidenceSeverity.MEDIUM,
                "network jitter", snapshot.getCapturedAtNanos(), Collections.<String, String>emptyMap()));
        }

        public void onUnload() {
            unloaded = true;
        }

        public String getName() {
            return "recording-check";
        }
    }
}
