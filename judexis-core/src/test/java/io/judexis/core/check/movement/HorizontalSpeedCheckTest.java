package io.judexis.core.check.movement;

import io.judexis.core.check.CheckContext;
import io.judexis.core.data.PlayerData;
import io.judexis.core.decision.DecisionEventBus;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.PredictionResult;
import io.judexis.core.physics.analysis.ErrorMeasurement;
import io.judexis.core.snapshot.MovementSnapshot;
import io.judexis.core.violation.EvidenceRouter;
import io.judexis.core.violation.ViolationPolicy;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HorizontalSpeedCheckTest {

    @Test
    void frictionTransitionFromIceToGroundTightensAllowance() {
        HorizontalSpeedCheck check = new HorizontalSpeedCheck(new HorizontalSpeedConfig());
        TestEnv env = buildEnv(80);

        MotionState previous = new MotionState(0.0D, 64.0D, 0.0D, 0.36D, 0.0D, 0.0D,
            true, false, true, false, true, false, 0.98D, 0, 4);
        MotionState current = new MotionState(0.0D, 64.0D, 0.0D, 0.33D, 0.0D, 0.0D,
            true, false, false, false, true, false, 0.6D, 0, 5);

        addHistory(env.data, 10L, previous);
        addHistory(env.data, 11L, current);

        MovementSnapshot observed = movement(11L, 0.45D, 0.0D, true);
        check.onSnapshot(env.context, observed);

        assertTrue(env.data.getAccumulator().getEvidenceCount() >= 1L);
    }

    @Test
    void liquidContextAllowsSmallExcessWithoutFlag() {
        HorizontalSpeedCheck check = new HorizontalSpeedCheck(new HorizontalSpeedConfig());
        TestEnv env = buildEnv(80);

        MotionState previous = new MotionState(0.0D, 64.0D, 0.0D, 0.18D, 0.0D, 0.0D,
            false, true, false, false, false, false, 0.91D, 2, 0);
        MotionState current = new MotionState(0.0D, 64.0D, 0.0D, 0.16D, 0.0D, 0.0D,
            false, true, false, false, false, false, 0.91D, 3, 0);

        addHistory(env.data, 20L, previous);
        addHistory(env.data, 21L, current);

        MovementSnapshot observed = movement(21L, 0.22D, 0.0D, false);
        check.onSnapshot(env.context, observed);

        assertEquals(0L, env.data.getAccumulator().getEvidenceCount());
    }

    private TestEnv buildEnv(int ticks) {
        PlayerData data = new PlayerData(64);
        for (int i = 0; i < ticks; i++) {
            data.getContextState().advanceTick();
        }
        data.getContextState().setTicksPerSecond(20.0D);
        data.getContextState().setPingEstimateMillis(40);
        EvidenceRouter router = new EvidenceRouter(new ViolationPolicy(1.0D, 500.0D, 500.0D), new DecisionEventBus());
        PlayerProfile profile = new PlayerProfile(UUID.randomUUID(), "tester", 1L);
        return new TestEnv(data, new CheckContext(profile, data, router, HorizontalSpeedCheck.ID));
    }

    private void addHistory(PlayerData data, long tick, MotionState state) {
        MovementSnapshot snapshot = movement(tick, state.getMotionX(), state.getMotionY(), state.isOnGround());
        PredictionResult prediction = new PredictionResult(state,
            state.getMotionX(), state.getMotionY(), state.getMotionZ(),
            state.getX(), state.getY(), state.getZ(), 0.01D, 0.03D);
        ErrorMeasurement measurement = new ErrorMeasurement(0.0D, 0.0D, 0.0D, 0.01D, 0.01D, 0.01D, 0.5D, 0.03D);
        data.setLatestErrorMeasurement(measurement);
        data.getMotionHistoryBuffer().add(snapshot, state, prediction, measurement);
    }

    private MovementSnapshot movement(long tick, double dx, double dy, boolean onGround) {
        return new MovementSnapshot(tick, tick * 1_000_000L,
            20.0D + dx, 65.0D + dy, 20.0D,
            20.0D, 65.0D, 20.0D,
            0.0F, 0.0F,
            onGround, onGround, false, false, false, false, onGround ? 0.6D : 0.91D);
    }

    private static final class TestEnv {
        private final PlayerData data;
        private final CheckContext context;

        private TestEnv(PlayerData data, CheckContext context) {
            this.data = data;
            this.context = context;
        }
    }
}
