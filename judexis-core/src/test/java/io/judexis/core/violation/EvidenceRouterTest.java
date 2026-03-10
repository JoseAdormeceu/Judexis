package io.judexis.core.violation;

import io.judexis.core.data.PlayerData;
import io.judexis.core.decision.DecisionEventBus;
import io.judexis.core.domain.PlayerProfile;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EvidenceRouterTest {

    @Test
    void routesEvidenceIntoAccumulatorAndCategoryTotals() {
        PlayerData data = new PlayerData(8);
        EvidenceRouter router = new EvidenceRouter(
            new ViolationPolicy(0.9D, 100.0D, 100.0D),
            new DecisionEventBus()
        );

        Evidence evidence = new Evidence("test", EvidenceType.MOVEMENT, EvidenceSeverity.HIGH,
            "step", 100L, Collections.singletonMap("k", "v"));
        router.route(new PlayerProfile(UUID.randomUUID(), "p", 1L), "check.step", evidence, data);

        assertEquals(5.0D, data.getAccumulator().getTotalViolationScore());
        assertEquals(5.0D, data.getPolicyState().snapshotCategoryTotals().get(EvidenceType.MOVEMENT));
    }
}
