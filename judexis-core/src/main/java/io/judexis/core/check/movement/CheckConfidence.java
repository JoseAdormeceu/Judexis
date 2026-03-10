package io.judexis.core.check.movement;

/**
 * Confidence model for movement checks based on runtime network/server quality.
 */
public final class CheckConfidence {
    private final double pingFactor;
    private final double tpsFactor;
    private final double combinedFactor;

    public CheckConfidence(double pingFactor, double tpsFactor) {
        this.pingFactor = clamp(pingFactor, 0.3D, 1.0D);
        this.tpsFactor = clamp(tpsFactor, 0.7D, 1.0D);
        this.combinedFactor = this.pingFactor * this.tpsFactor;
    }

    public static CheckConfidence fromRuntime(int pingMillis, int maxPing, double tps, double idealTps) {
        double ping = 1.0D - ((double) pingMillis / (double) maxPing);
        double tpsFactor = tps / idealTps;
        return new CheckConfidence(ping, tpsFactor);
    }

    public double getPingFactor() { return pingFactor; }
    public double getTpsFactor() { return tpsFactor; }
    public double getCombinedFactor() { return combinedFactor; }

    private static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}
