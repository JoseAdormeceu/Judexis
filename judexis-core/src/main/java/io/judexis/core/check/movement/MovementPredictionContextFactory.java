package io.judexis.core.check.movement;

import io.judexis.core.check.CheckContext;
import io.judexis.core.context.ContextState;
import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.history.MotionHistoryEntry;
import io.judexis.core.snapshot.MovementSnapshot;

/**
 * Builds movement context from check/runtime state.
 */
public final class MovementPredictionContextFactory {
    private MovementPredictionContextFactory() {
    }

    public static MovementPredictionContext create(CheckContext context,
                                                   MovementSnapshot movementSnapshot,
                                                   int maxPing,
                                                   double idealTps,
                                                   int velocityGraceTicks,
                                                   int teleportGraceTicks) {
        MotionHistoryEntry currentEntry = context.getMotionHistoryBuffer().getRelative(0);
        MotionHistoryEntry previousEntry = context.getMotionHistoryBuffer().getRelative(1);
        MotionState current = currentEntry == null ? null : currentEntry.getPredictedState();
        MotionState previous = previousEntry == null ? null : previousEntry.getPredictedState();

        ContextState state = context.getContextState();
        CheckConfidence confidence = CheckConfidence.fromRuntime(
            state.getPingEstimateMillis(),
            maxPing,
            state.getTicksPerSecond(),
            idealTps
        );

        return new MovementPredictionContext(
            current,
            previous,
            movementSnapshot.deltaX(),
            movementSnapshot.deltaY(),
            movementSnapshot.deltaZ(),
            state.getVelocityTicks() < velocityGraceTicks,
            state.getTeleportTicks() < teleportGraceTicks,
            confidence
        );
    }
}
