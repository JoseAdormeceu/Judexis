package io.judexis.core.check.movement;

import io.judexis.core.check.Check;
import io.judexis.core.check.CheckContext;
import io.judexis.core.context.ContextState;
import io.judexis.core.physics.MotionState;
import io.judexis.core.snapshot.MovementSnapshot;
import io.judexis.core.snapshot.Snapshot;
import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.EvidenceSeverity;
import io.judexis.core.violation.EvidenceType;

import java.util.Map;

/**
 * Detects horizontal speed excess against environment-aware predicted motion.
 */
public final class HorizontalSpeedCheck implements Check {
    public static final String ID = "move-horizontal-speed";

    private final HorizontalSpeedConfig config;

    public HorizontalSpeedCheck(HorizontalSpeedConfig config) {
        this.config = config;
    }

    public void onLoad() {
    }

    public void onSnapshot(CheckContext context, Snapshot snapshot) {
        if (!(snapshot instanceof MovementSnapshot)) {
            return;
        }
        MovementSnapshot movementSnapshot = (MovementSnapshot) snapshot;
        if (shouldSkip(context.getContextState(), context)) {
            return;
        }

        MovementPredictionContext prediction = MovementPredictionContextFactory.create(
            context,
            movementSnapshot,
            config.getMaxPing(),
            config.getIdealTps(),
            config.getVelocityGraceTicks(),
            config.getTeleportGraceTicks()
        );
        if (prediction.isRecentVelocity() || prediction.isRecentTeleport()) {
            return;
        }

        MotionState current = prediction.getCurrent();
        MotionState previous = prediction.getPrevious();
        if (current == null || previous == null) {
            return;
        }

        double observedHorizontal = Math.sqrt((prediction.getObservedDeltaX() * prediction.getObservedDeltaX())
            + (prediction.getObservedDeltaZ() * prediction.getObservedDeltaZ()));
        double predictedHorizontal = Math.sqrt((current.getMotionX() * current.getMotionX())
            + (current.getMotionZ() * current.getMotionZ()));
        double previousHorizontal = Math.sqrt((previous.getMotionX() * previous.getMotionX())
            + (previous.getMotionZ() * previous.getMotionZ()));

        double environmentProjected = current.isNearGround()
            ? previousHorizontal * current.getSurfaceFriction() * 0.91D
            : previousHorizontal * 0.91D;

        double allowance = Math.max(predictedHorizontal, environmentProjected) + config.getBaseAllowance();
        if (current.isInLiquid()) {
            allowance += 0.08D;
        }
        if (current.isOnIce()) {
            allowance += 0.04D;
        }
        if (current.isOnSlime()) {
            allowance += 0.03D;
        }
        if (current.isTouchingBlockAbove()) {
            allowance += 0.02D;
        }

        double excess = observedHorizontal - allowance;
        if (excess <= 0.0D) {
            return;
        }

        double severityScore = Math.min(10.0D, excess * 25.0D) * prediction.getConfidence().getCombinedFactor();
        Map<String, String> metadata = MovementDebugMetadata.base(context, prediction, "HORIZONTAL_SPEED", severityScore);
        metadata.put("observedHorizontal", String.format("%.4f", observedHorizontal));
        metadata.put("predictedHorizontal", String.format("%.4f", predictedHorizontal));
        metadata.put("allowance", String.format("%.4f", allowance));
        metadata.put("excess", String.format("%.4f", excess));

        context.emit(new Evidence(getName(), EvidenceType.MOVEMENT, mapSeverity(severityScore),
            "HORIZONTAL_SPEED", movementSnapshot.getCapturedAtNanos(), metadata));
    }

    private boolean shouldSkip(ContextState state, CheckContext context) {
        if (state.getJoinTicks() < config.getJoinGraceTicks()) {
            return true;
        }
        if (state.getWorldChangeTicks() < config.getWorldChangeGraceTicks()) {
            return true;
        }
        return context.getMotionHistoryBuffer().size() < config.getMinHistory();
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

    public void onUnload() {
    }

    public String getName() {
        return ID;
    }
}
