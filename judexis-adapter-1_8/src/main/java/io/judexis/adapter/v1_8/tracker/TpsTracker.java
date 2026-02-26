package io.judexis.adapter.v1_8.tracker;

/**
 * Moving-average TPS tracker based on tick-to-tick monotonic durations.
 */
public final class TpsTracker {
    private final long[] durationsNanos;
    private int cursor;
    private int samples;
    private long lastTickNanos;

    public TpsTracker(int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("windowSize must be positive");
        }
        this.durationsNanos = new long[windowSize];
        this.lastTickNanos = -1L;
    }

    /**
     * Records a tick and returns current TPS estimate.
     *
     * @return moving average TPS capped at 20.0
     */
    public double recordTickAndEstimate() {
        long now = System.nanoTime();
        if (lastTickNanos != -1L) {
            long delta = now - lastTickNanos;
            if (delta > 0L) {
                durationsNanos[cursor] = delta;
                cursor++;
                if (cursor >= durationsNanos.length) {
                    cursor = 0;
                }
                if (samples < durationsNanos.length) {
                    samples++;
                }
            }
        }
        lastTickNanos = now;
        return estimateTps();
    }

    /**
     * @return average TPS capped between 1 and 20
     */
    public double estimateTps() {
        if (samples == 0) {
            return 20.0D;
        }
        long sum = 0L;
        for (int i = 0; i < samples; i++) {
            sum += durationsNanos[i];
        }
        if (sum <= 0L) {
            return 20.0D;
        }
        double avgNanos = (double) sum / (double) samples;
        double tps = 1_000_000_000.0D / avgNanos;
        if (tps > 20.0D) {
            return 20.0D;
        }
        if (tps < 1.0D) {
            return 1.0D;
        }
        return tps;
    }
}
