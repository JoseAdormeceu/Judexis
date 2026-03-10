package io.judexis.core.physics.analysis;

/**
 * Immutable error comparison output between real and predicted movement.
 */
public final class ErrorMeasurement {
    private final double deltaX;
    private final double deltaY;
    private final double deltaZ;
    private final double rawHorizontalError;
    private final double rawVerticalError;
    private final double totalError;
    private final double normalizedError;
    private final double tolerance;

    public ErrorMeasurement(double deltaX, double deltaY, double deltaZ,
                            double rawHorizontalError, double rawVerticalError, double totalError,
                            double normalizedError, double tolerance) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.rawHorizontalError = rawHorizontalError;
        this.rawVerticalError = rawVerticalError;
        this.totalError = totalError;
        this.normalizedError = normalizedError;
        this.tolerance = tolerance;
    }

    public double getDeltaX() { return deltaX; }
    public double getDeltaY() { return deltaY; }
    public double getDeltaZ() { return deltaZ; }
    public double getRawHorizontalError() { return rawHorizontalError; }
    public double getRawVerticalError() { return rawVerticalError; }
    public double getTotalError() { return totalError; }
    public double getNormalizedError() { return normalizedError; }
    public double getTolerance() { return tolerance; }

    public boolean isWithinTolerance() {
        return totalError <= tolerance;
    }
}
