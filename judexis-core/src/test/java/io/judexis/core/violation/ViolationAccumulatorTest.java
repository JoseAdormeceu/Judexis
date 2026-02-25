package io.judexis.core.violation;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViolationAccumulatorTest {

    @Test
    void appendsEvidenceAndTracksScore() {
        ViolationAccumulator accumulator = new ViolationAccumulator(2);

        accumulator.append(new Evidence("sample-check", EvidenceType.MOVEMENT, EvidenceSeverity.LOW,
            "first", 1L, Collections.singletonMap("key", "value")));
        accumulator.append(new Evidence("sample-check", EvidenceType.MOVEMENT, EvidenceSeverity.HIGH,
            "second", 2L, Collections.singletonMap("k2", "v2")));

        assertEquals(6.0D, accumulator.getTotalViolationScore());
        assertEquals(2L, accumulator.getEvidenceCount());
        assertEquals(2, accumulator.snapshotRecentEvidence().size());
    }

    @Test
    void respectsRecentEvidenceCapacity() {
        ViolationAccumulator accumulator = new ViolationAccumulator(1);

        accumulator.append(new Evidence("sample-check", EvidenceType.NETWORK, EvidenceSeverity.LOW,
            "first", 1L, null));
        accumulator.append(new Evidence("sample-check", EvidenceType.NETWORK, EvidenceSeverity.MEDIUM,
            "second", 2L, null));

        assertEquals(1, accumulator.snapshotRecentEvidence().size());
        assertEquals("second", accumulator.snapshotRecentEvidence().get(0).getDetail());
    }
}
