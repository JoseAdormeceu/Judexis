package io.judexis.core.check.movement;

import io.judexis.core.check.Check;
import io.judexis.core.check.CheckContext;
import io.judexis.core.context.ContextState;
import io.judexis.core.physics.analysis.ErrorAccumulator;
import io.judexis.core.physics.analysis.ErrorMeasurement;
import io.judexis.core.physics.history.MotionHistoryBuffer;
import io.judexis.core.physics.history.MotionHistoryEntry;
import io.judexis.core.snapshot.MovementSnapshot;
import io.judexis.core.snapshot.Snapshot;
import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.EvidenceSeverity;
import io.judexis.core.violation.EvidenceType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Detects sustained inconsistencies between reported grounded state and vertical prediction model.
 */
public final class VerticalIntegrityCheck implements Check {
    public static final String ID = "move-vertical-integrity";

    private final VerticalIntegrityConfig config;

    public VerticalIntegrityCheck(VerticalIntegrityConfig config) {
        this.config = config;
    }

    public void onLoad() {
    }

    public void onSnapshot(CheckContext context, Snapshot snapshot) {
        if (!(snapshot instanceof MovementSnapshot)) {
            return;
        }
        MovementSnapshot movementSnapshot = (MovementSnapshot) snapshot;
        if (shouldSkip(context.getContextState(), context.getMotionHistoryBuffer())) {
            return;
        }

        AnalysisWindow window = analyzeWindow(context.getMotionHistoryBuffer());
        if (window == null) {
            return;
        }

        ErrorAccumulator accumulator = context.getErrorAccumulator();
        double verticalScore = accumulator.getVerticalScore();
        double totalScore = accumulator.getCurrentScore();

        boolean mismatchPattern = window.mismatchCount >= config.getMinMismatches();
        boolean driftPattern = verticalScore >= config.getVerticalScoreThreshold() * config.getDecaySensitivity();

        if (!mismatchPattern && !driftPattern) {
            return;
        }

        String signal = mismatchPattern ? "GROUND_MISMATCH" : "VERTICAL_DRIFT";
        double severityScore = severityScore(window, verticalScore);

        Map<String, String> metadata = buildMetadata(context, window, verticalScore, totalScore, signal, severityScore);
        EvidenceSeverity severity = mapSeverity(severityScore);

        context.emit(new Evidence(getName(), EvidenceType.MOVEMENT, severity,
            signal, movementSnapshot.getCapturedAtNanos(), metadata));
    }

    public void onUnload() {
    }

    public String getName() {
        return ID;
    }

    boolean shouldSkip(ContextState state, MotionHistoryBuffer history) {
        if (state.getJoinTicks() < config.getJoinGraceTicks()) {
            return true;
        }
        if (state.getTeleportTicks() < config.getTeleportGraceTicks()) {
            return true;
        }
        if (state.getVelocityTicks() < config.getVelocityGraceTicks()) {
            return true;
        }
        if (state.getWorldChangeTicks() < config.getWorldChangeGraceTicks()) {
            return true;
        }
        if (state.getTicksPerSecond() < config.getMinTps()) {
            return true;
        }
        if (state.getPingEstimateMillis() > config.getMaxPing()) {
            return true;
        }
        return history.size() < config.getMinHistory();
    }

    private AnalysisWindow analyzeWindow(MotionHistoryBuffer history) {
        int windowSize = Math.min(config.getPatternWindow(), history.size());
        if (windowSize <= 0) {
            return null;
        }

        int mismatches = 0;
        int counted = 0;
        double verticalRawSum = 0.0D;
        double normalizedSum = 0.0D;

        for (int i = 0; i < windowSize; i++) {
            MotionHistoryEntry entry = history.getRelative(i);
            if (entry == null || entry.getErrorMeasurement() == null || entry.getPredictedState() == null) {
                continue;
            }
            ErrorMeasurement m = entry.getErrorMeasurement();
            counted++;
            verticalRawSum += m.getRawVerticalError();
            normalizedSum += m.getNormalizedError();

            if (entry.getSnapshot().isOnGround() && !entry.getPredictedState().isOnGround()
                && m.getNormalizedError() >= config.getNormalizedErrorThreshold()) {
                mismatches++;
            }
        }

        if (counted == 0) {
            return null;
        }

        return new AnalysisWindow(windowSize, mismatches,
            verticalRawSum / counted,
            normalizedSum / counted);
    }

    private double severityScore(AnalysisWindow window, double verticalScore) {
        double mismatchRatio = (double) window.mismatchCount / (double) window.windowSize;
        double mismatchComponent = mismatchRatio * 6.5D;
        double verticalComponent = Math.min(3.5D, verticalScore * 3.0D);
        double averageComponent = Math.min(2.0D, window.avgVerticalNormalizedError);
        double score = mismatchComponent + verticalComponent + averageComponent;
        if (score < 0.0D) {
            return 0.0D;
        }
        if (score > 10.0D) {
            return 10.0D;
        }
        return score;
    }

    private EvidenceSeverity mapSeverity(double severityScore) {
        if (severityScore >= 8.5D) {
            return EvidenceSeverity.CRITICAL;
        }
        if (severityScore >= 6.0D) {
            return EvidenceSeverity.HIGH;
        }
        if (severityScore >= 3.0D) {
            return EvidenceSeverity.MEDIUM;
        }
        return EvidenceSeverity.LOW;
    }

    private Map<String, String> buildMetadata(CheckContext context, AnalysisWindow window,
                                              double verticalScore, double totalScore,
                                              String signal, double severityScore) {
        Map<String, String> metadata = new LinkedHashMap<String, String>();
        metadata.put("signal", signal);
        metadata.put("ping", String.valueOf(context.getContextState().getPingEstimateMillis()));
        metadata.put("tps", format(context.getContextState().getTicksPerSecond()));
        metadata.put("window", String.valueOf(window.windowSize));
        metadata.put("mismatches", String.valueOf(window.mismatchCount));
        metadata.put("avgVerticalRawError", format(window.avgVerticalRawError));
        metadata.put("avgVerticalNormalizedError", format(window.avgVerticalNormalizedError));
        metadata.put("verticalScore", format(verticalScore));
        metadata.put("totalScore", format(totalScore));
        metadata.put("severityScore", format(severityScore));

        MotionHistoryBuffer history = context.getMotionHistoryBuffer();
        for (int i = 0; i < 3; i++) {
            MotionHistoryEntry entry = history.getRelative(i);
            if (entry == null || entry.getErrorMeasurement() == null) {
                continue;
            }
            ErrorMeasurement measurement = entry.getErrorMeasurement();
            metadata.put("delta" + i,
                format(measurement.getDeltaX()) + "," +
                    format(measurement.getDeltaY()) + "," +
                    format(measurement.getDeltaZ()));
        }
        return metadata;
    }

    private String format(double value) {
        return String.format("%.4f", value);
    }

    private static final class AnalysisWindow {
        private final int windowSize;
        private final int mismatchCount;
        private final double avgVerticalRawError;
        private final double avgVerticalNormalizedError;

        private AnalysisWindow(int windowSize, int mismatchCount, double avgVerticalRawError, double avgVerticalNormalizedError) {
            this.windowSize = windowSize;
            this.mismatchCount = mismatchCount;
            this.avgVerticalRawError = avgVerticalRawError;
            this.avgVerticalNormalizedError = avgVerticalNormalizedError;
        }
    }
}
