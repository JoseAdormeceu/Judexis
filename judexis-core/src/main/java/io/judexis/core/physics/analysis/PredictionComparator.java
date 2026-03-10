package io.judexis.core.physics.analysis;

import io.judexis.core.context.ContextState;
import io.judexis.core.physics.PredictionResult;
import io.judexis.core.snapshot.MovementSnapshot;

/**
 * Computes normalized prediction errors for movement analysis.
 */
public final class PredictionComparator {
    private static final double BASE_TOLERANCE = 0.03D;
    private static final double AIR_HORIZONTAL_BONUS = 0.015D;

    public ErrorMeasurement computeError(MovementSnapshot realSnapshot,
                                         PredictionResult predictionResult,
                                         ContextState contextState) {
        double deltaX = realSnapshot.getX() - predictionResult.getPredictedX();
        double deltaY = realSnapshot.getY() - predictionResult.getPredictedY();
        double deltaZ = realSnapshot.getZ() - predictionResult.getPredictedZ();

        double horizontal = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));
        double vertical = Math.abs(deltaY);
        double total = Math.sqrt((horizontal * horizontal) + (vertical * vertical));

        double tolerance = BASE_TOLERANCE;

        int ping = contextState.getPingEstimateMillis();
        tolerance += Math.min(0.12D, ping * 0.00020D);

        double tpsDeficit = 20.0D - contextState.getTicksPerSecond();
        if (tpsDeficit > 0.0D) {
            tolerance += Math.min(0.12D, tpsDeficit * 0.006D);
        }

        if (!realSnapshot.isOnGround()) {
            tolerance += AIR_HORIZONTAL_BONUS;
        }

        double normalized = tolerance <= 0.0D ? total : total / tolerance;

        return new ErrorMeasurement(
            deltaX,
            deltaY,
            deltaZ,
            horizontal,
            vertical,
            total,
            normalized,
            tolerance
        );
    }
}
