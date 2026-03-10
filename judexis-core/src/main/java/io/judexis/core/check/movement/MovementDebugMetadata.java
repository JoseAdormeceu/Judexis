package io.judexis.core.check.movement;

import io.judexis.core.check.CheckContext;
import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.analysis.ErrorMeasurement;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Standard metadata formatter for movement checks.
 */
public final class MovementDebugMetadata {
    private MovementDebugMetadata() {
    }

    public static Map<String, String> base(CheckContext context,
                                           MovementPredictionContext prediction,
                                           String signal,
                                           double severityScore) {
        Map<String, String> metadata = new LinkedHashMap<String, String>();
        metadata.put("signal", signal);

        MotionState predicted = prediction.getCurrent();
        if (predicted != null) {
            metadata.put("predicted", fmt(predicted.getMotionX()) + "," + fmt(predicted.getMotionY()) + "," + fmt(predicted.getMotionZ()));
            metadata.put("environment",
                "ground=" + predicted.isOnGround() +
                    ",nearGround=" + predicted.isNearGround() +
                    ",liquid=" + predicted.isInLiquid() +
                    ",ice=" + predicted.isOnIce() +
                    ",slime=" + predicted.isOnSlime() +
                    ",ceiling=" + predicted.isTouchingBlockAbove() +
                    ",friction=" + fmt(predicted.getSurfaceFriction()));
        }

        metadata.put("observed", fmt(prediction.getObservedDeltaX()) + "," + fmt(prediction.getObservedDeltaY()) + "," + fmt(prediction.getObservedDeltaZ()));

        ErrorMeasurement error = context.getLatestErrorMeasurement();
        if (error != null) {
            metadata.put("error", fmt(error.getRawHorizontalError()) + "," + fmt(error.getRawVerticalError()) + "," + fmt(error.getTotalError()));
            metadata.put("tolerance", fmt(error.getTolerance()));
        }

        metadata.put("confidence", fmt(prediction.getConfidence().getCombinedFactor()));
        metadata.put("severityScore", fmt(severityScore));
        return metadata;
    }

    private static String fmt(double value) {
        return String.format("%.4f", value);
    }
}
