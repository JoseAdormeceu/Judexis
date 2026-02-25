package io.judexis.core.check;

import io.judexis.core.data.PlayerData;
import io.judexis.core.decision.DecisionEventBus;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.snapshot.NetworkSnapshot;
import io.judexis.core.snapshot.Snapshot;
import io.judexis.core.violation.EvidenceRouter;
import io.judexis.core.violation.ViolationPolicy;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckManagerTest {

    @Test
    void canEnableAndDisableRegisteredCheck() {
        CheckRegistry registry = new CheckRegistry();
        CheckConfiguration config = new CheckConfiguration();
        CheckManager manager = new CheckManager(registry, config,
            new EvidenceRouter(new ViolationPolicy(0.9D, 10.0D, 20.0D), new DecisionEventBus()));

        CountingCheck check = new CountingCheck();
        manager.register("test.check", CheckCategory.MISC, check);

        assertTrue(manager.isEnabled("test.check"));
        assertEquals(1, check.loadCalls);

        manager.setEnabled("test.check", false);
        assertFalse(manager.isEnabled("test.check"));
        assertEquals(1, check.unloadCalls);

        manager.setEnabled("test.check", true);
        assertTrue(manager.isEnabled("test.check"));
        assertEquals(2, check.loadCalls);

        manager.dispatch(new PlayerProfile(UUID.randomUUID(), "alpha", 1L), new PlayerData(16),
            new NetworkSnapshot(1L, 2L, 50, 20, 0, 0));
        assertEquals(1, check.snapshotCalls);
    }

    private static final class CountingCheck implements Check {
        private int loadCalls;
        private int unloadCalls;
        private int snapshotCalls;

        public void onLoad() {
            loadCalls++;
        }

        public void onSnapshot(CheckContext context, Snapshot snapshot) {
            snapshotCalls++;
        }

        public void onUnload() {
            unloadCalls++;
        }

        public String getName() {
            return "counting";
        }
    }
}
