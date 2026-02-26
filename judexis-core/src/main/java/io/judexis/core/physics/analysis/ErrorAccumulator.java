package io.judexis.core.physics.analysis;

/**
 * Sliding-window score accumulator with deterministic decay.
 */
public final class ErrorAccumulator {
    private static final int DEFAULT_WINDOW = 20;
    private static final double DEFAULT_DECAY = 0.95D;

    private final double[] horizontalWindow;
    private final double[] verticalWindow;
    private final int windowSize;
    private final double decay;

    private int cursor;
    private int count;

    private double horizontalWindowSum;
    private double verticalWindowSum;

    private double horizontalScore;
    private double verticalScore;
    private double totalScore;

    public ErrorAccumulator() {
        this(DEFAULT_WINDOW, DEFAULT_DECAY);
    }

    public ErrorAccumulator(int windowSize, double decay) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("windowSize must be positive");
        }
        if (decay <= 0.0D || decay > 1.0D) {
            throw new IllegalArgumentException("decay must be in (0,1]");
        }
        this.windowSize = windowSize;
        this.decay = decay;
        this.horizontalWindow = new double[windowSize];
        this.verticalWindow = new double[windowSize];
    }

    public void addMeasurement(ErrorMeasurement measurement) {
        if (count == windowSize) {
            horizontalWindowSum -= horizontalWindow[cursor];
            verticalWindowSum -= verticalWindow[cursor];
        } else {
            count++;
        }

        horizontalWindow[cursor] = measurement.getRawHorizontalError();
        verticalWindow[cursor] = measurement.getRawVerticalError();
        horizontalWindowSum += horizontalWindow[cursor];
        verticalWindowSum += verticalWindow[cursor];

        cursor++;
        if (cursor >= windowSize) {
            cursor = 0;
        }

        horizontalScore = (horizontalScore * decay) + measurement.getRawHorizontalError();
        verticalScore = (verticalScore * decay) + measurement.getRawVerticalError();
        totalScore = horizontalScore + verticalScore;
    }

    public void tickDecay() {
        horizontalScore = horizontalScore * decay;
        verticalScore = verticalScore * decay;
        totalScore = horizontalScore + verticalScore;
    }

    public double getHorizontalScore() { return horizontalScore; }
    public double getVerticalScore() { return verticalScore; }
    public double getCurrentScore() { return totalScore; }
    public double getWindowHorizontalSum() { return horizontalWindowSum; }
    public double getWindowVerticalSum() { return verticalWindowSum; }

    public boolean exceedsThreshold(double threshold) {
        return totalScore >= threshold;
    }
}
