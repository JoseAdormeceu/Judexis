package io.judexis.core.check;

import io.judexis.core.context.ContextState;
import io.judexis.core.data.PlayerData;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.physics.analysis.ErrorAccumulator;
import io.judexis.core.physics.analysis.ErrorMeasurement;
import io.judexis.core.physics.history.MotionHistoryBuffer;
import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.EvidenceRouter;

/**
 * Context passed to checks on each dispatch.
 */
public final class CheckContext {
    private final PlayerProfile profile;
    private final PlayerData playerData;
    private final EvidenceRouter evidenceRouter;
    private final String checkId;

    public CheckContext(PlayerProfile profile, PlayerData playerData, EvidenceRouter evidenceRouter, String checkId) {
        this.profile = profile;
        this.playerData = playerData;
        this.evidenceRouter = evidenceRouter;
        this.checkId = checkId;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public ContextState getContextState() {
        return playerData.getContextState();
    }

    public MotionHistoryBuffer getMotionHistoryBuffer() {
        return playerData.getMotionHistoryBuffer();
    }

    public ErrorAccumulator getErrorAccumulator() {
        return playerData.getErrorAccumulator();
    }

    public ErrorMeasurement getLatestErrorMeasurement() {
        return playerData.getLatestErrorMeasurement();
    }

    public void emit(Evidence evidence) {
        evidenceRouter.route(profile, checkId, evidence, playerData);
    }

    public <T> T getCheckState(String stateKey, Class<T> type) {
        Object state = playerData.getCheckState(checkId + ":" + stateKey);
        if (state == null) {
            return null;
        }
        return type.cast(state);
    }

    public void setCheckState(String stateKey, Object state) {
        playerData.setCheckState(checkId + ":" + stateKey, state);
    }
}
