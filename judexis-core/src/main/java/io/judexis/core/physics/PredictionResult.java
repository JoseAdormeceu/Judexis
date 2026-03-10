package io.judexis.core.physics;

/**
 * Result of a single physics simulation step.
 */
public final class PredictionResult {
    private final MotionState predictedState;
    private final double predictedMotionX;
    private final double predictedMotionY;
    private final double predictedMotionZ;
    private final double predictedX;
    private final double predictedY;
    private final double predictedZ;
    private final double deltaError;
    private final double toleranceMargin;

    public PredictionResult(MotionState predictedState,
                            double predictedMotionX, double predictedMotionY, double predictedMotionZ,
                            double predictedX, double predictedY, double predictedZ,
                            double deltaError, double toleranceMargin) {
        this.predictedState = predictedState;
        this.predictedMotionX = predictedMotionX;
        this.predictedMotionY = predictedMotionY;
        this.predictedMotionZ = predictedMotionZ;
        this.predictedX = predictedX;
        this.predictedY = predictedY;
        this.predictedZ = predictedZ;
        this.deltaError = deltaError;
        this.toleranceMargin = toleranceMargin;
    }

    public MotionState getPredictedState() { return predictedState; }
    public double getPredictedMotionX() { return predictedMotionX; }
    public double getPredictedMotionY() { return predictedMotionY; }
    public double getPredictedMotionZ() { return predictedMotionZ; }
    public double getPredictedX() { return predictedX; }
    public double getPredictedY() { return predictedY; }
    public double getPredictedZ() { return predictedZ; }
    public double getDeltaError() { return deltaError; }
    public double getToleranceMargin() { return toleranceMargin; }

    public boolean isWithinTolerance() {
        return deltaError <= toleranceMargin;
    }
}
