package io.judexis.core.check.movement;

import io.judexis.core.check.CheckContext;
import io.judexis.core.data.PlayerData;
import io.judexis.core.decision.DecisionEventBus;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.PredictionResult;
import io.judexis.core.physics.analysis.ErrorAccumulator;
import io.judexis.core.physics.analysis.ErrorMeasurement;
import io.judexis.core.snapshot.MovementSnapshot;
import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.EvidenceRouter;
import io.judexis.core.violation.ViolationPolicy;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerticalIntegrityCheckTest {

    @Test
    void guardClausesSkipDuringJoinGrace() {
        VerticalIntegrityCheck check = new VerticalIntegrityCheck(new VerticalIntegrityConfig());
        TestEnv env = buildEnv(5, 20.0D, 50, 12, 0.7D);

        check.onSnapshot(env.context, movement(100L, true));

        assertEquals(0L, env.playerData.getAccumulator().getEvidenceCount());
    }

    @Test
    void patternDetectionRequiresMultipleTicks() {
        VerticalIntegrityCheck check = new VerticalIntegrityCheck(new VerticalIntegrityConfig());
        TestEnv env = buildEnv(80, 20.0D, 80, 10, 0.5D);

        fillHistory(env.playerData, 10, 5, 1.2D, true);
        check.onSnapshot(env.context, movement(101L, true));

        assertEquals(0L, env.playerData.getAccumulator().getEvidenceCount());
    }

    @Test
    void evidenceEmitsOnlyAfterSustainedMismatch() {
        VerticalIntegrityCheck check = new VerticalIntegrityCheck(new VerticalIntegrityConfig());
        TestEnv env = buildEnv(80, 20.0D, 80, 10, 0.7D);

        fillHistory(env.playerData, 10, 6, 1.25D, true);
        check.onSnapshot(env.context, movement(200L, true));

        assertTrue(env.playerData.getAccumulator().getEvidenceCount() >= 1L);
        Evidence last = lastEvidence(env.playerData);
        String signal = last.getMetadata().get("signal");
        assertTrue("GROUND_MISMATCH".equals(signal) || "VERTICAL_DRIFT".equals(signal));
    }

    @Test
    void severityScalesWithScore() {
        VerticalIntegrityCheck check = new VerticalIntegrityCheck(new VerticalIntegrityConfig());
        TestEnv low = buildEnv(80, 20.0D, 80, 10, 0.3D);
        fillHistory(low.playerData, 10, 6, 1.1D, true);
        check.onSnapshot(low.context, movement(300L, true));

        TestEnv high = buildEnv(80, 20.0D, 80, 10, 1.6D);
        fillHistory(high.playerData, 10, 9, 1.8D, true);
        check.onSnapshot(high.context, movement(301L, true));

        Evidence lowEvidence = lastEvidence(low.playerData);
        Evidence highEvidence = lastEvidence(high.playerData);

        assertTrue(highEvidence.getSeverity().ordinal() >= lowEvidence.getSeverity().ordinal());
    }

    private TestEnv buildEnv(int joinTicks, double tps, int ping, int historySize, double verticalSeedScore) {
        PlayerData data = new PlayerData(64);
        data.getContextState().setTicksPerSecond(tps);
        data.getContextState().setPingEstimateMillis(ping);
        for (int i = 0; i < joinTicks; i++) {
            data.getContextState().advanceTick();
        }

        ErrorAccumulator accumulator = data.getErrorAccumulator();
        for (int i = 0; i < historySize; i++) {
            accumulator.addMeasurement(new ErrorMeasurement(0.0D, 0.0D, 0.0D,
                0.02D, verticalSeedScore / historySize, 0.0D, 1.0D, 0.03D));
        }

        EvidenceRouter router = new EvidenceRouter(new ViolationPolicy(1.0D, 500.0D, 500.0D), new DecisionEventBus());
        PlayerProfile profile = new PlayerProfile(UUID.randomUUID(), "tester", 1L);
        CheckContext context = new CheckContext(profile, data, router, VerticalIntegrityCheck.ID);
        return new TestEnv(context, data);
    }

    private void fillHistory(PlayerData data, int window, int mismatches, double normalizedError, boolean reportedGround) {
        for (int i = 0; i < window; i++) {
            boolean mismatch = i < mismatches;
            MovementSnapshot snapshot = movement(i + 1L, reportedGround);
            MotionState predicted = new MotionState(snapshot.getX(), snapshot.getY(), snapshot.getZ(),
                snapshot.deltaX(), snapshot.deltaY(), snapshot.deltaZ(), !mismatch, false, false, false, 1, 0);
            ErrorMeasurement measurement = new ErrorMeasurement(0.01D, 0.06D, 0.01D,
                0.014D, 0.06D, 0.062D, mismatch ? normalizedError : 0.7D, 0.03D);
            PredictionResult prediction = new PredictionResult(predicted,
                predicted.getMotionX(), predicted.getMotionY(), predicted.getMotionZ(),
                predicted.getX(), predicted.getY(), predicted.getZ(), 0.062D, 0.03D);
            data.getMotionHistoryBuffer().add(snapshot, predicted, prediction, measurement);
            data.setLatestErrorMeasurement(measurement);
        }
    }

    private MovementSnapshot movement(long tick, boolean onGround) {
        return new MovementSnapshot(tick, tick * 1_000_000L, 10.0D + tick, 70.0D, 10.0D,
            9.8D + tick, 70.0D, 10.0D, 0.0F, 0.0F, onGround);
    }

    private Evidence lastEvidence(PlayerData data) {
        List<Evidence> evidence = data.getAccumulator().snapshotRecentEvidence();
        return evidence.get(evidence.size() - 1);
    }

    private static final class TestEnv {
        private final CheckContext context;
        private final PlayerData playerData;

        private TestEnv(CheckContext context, PlayerData playerData) {
            this.context = context;
            this.playerData = playerData;
        }
    }
}
