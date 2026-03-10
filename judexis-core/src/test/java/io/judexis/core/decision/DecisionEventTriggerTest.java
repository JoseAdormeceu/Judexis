package io.judexis.core.decision;

import io.judexis.core.data.PlayerData;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.EvidenceRouter;
import io.judexis.core.violation.EvidenceSeverity;
import io.judexis.core.violation.EvidenceType;
import io.judexis.core.violation.ViolationPolicy;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecisionEventTriggerTest {

    @Test
    void emitsAlertWhenThresholdIsReached() {
        DecisionEventBus bus = new DecisionEventBus();
        AtomicInteger count = new AtomicInteger();
        bus.subscribe(event -> {
            if (event.getType() == DecisionEventType.ALERT) {
                count.incrementAndGet();
            }
        });

        EvidenceRouter router = new EvidenceRouter(new ViolationPolicy(1.0D, 4.0D, 100.0D), bus);
        PlayerData data = new PlayerData(16);
        PlayerProfile profile = new PlayerProfile(UUID.randomUUID(), "test", 1L);

        router.route(profile, "check.a", new Evidence("check.a", EvidenceType.NETWORK, EvidenceSeverity.HIGH,
            "one", 1L, Collections.<String, String>emptyMap()), data);

        assertEquals(1, count.get());
    }
}
