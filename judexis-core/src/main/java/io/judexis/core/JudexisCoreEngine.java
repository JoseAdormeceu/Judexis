package io.judexis.core;

import io.judexis.core.check.Check;
import io.judexis.core.check.CheckCategory;
import io.judexis.core.check.CheckConfiguration;
import io.judexis.core.check.CheckManager;
import io.judexis.core.check.CheckRegistry;
import io.judexis.core.data.PlayerData;
import io.judexis.core.data.PlayerDataStore;
import io.judexis.core.debug.CoreDebugSnapshot;
import io.judexis.core.decision.Decision;
import io.judexis.core.decision.DecisionEngine;
import io.judexis.core.decision.DecisionEventListener;
import io.judexis.core.decision.DecisionEventBus;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.pipeline.DefaultInputBus;
import io.judexis.core.pipeline.InputBus;
import io.judexis.core.snapshot.Snapshot;
import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.EvidenceRouter;
import io.judexis.core.violation.ViolationPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * Core composition root that wires runtime stores, checks, policy, and input bus.
 */
public final class JudexisCoreEngine {
    private final PlayerDataStore playerDataStore;
    private final CheckManager checkManager;
    private final DecisionEventBus decisionEventBus;
    private final InputBus inputBus;

    public JudexisCoreEngine(DecisionEngine decisionEngine, int evidenceCapacityPerPlayer) {
        this.playerDataStore = new PlayerDataStore(evidenceCapacityPerPlayer);
        this.decisionEventBus = new DecisionEventBus();
        EvidenceRouter evidenceRouter = new EvidenceRouter(new ViolationPolicy(0.92D, 10.0D, 20.0D), decisionEventBus);
        this.checkManager = new CheckManager(new CheckRegistry(), new CheckConfiguration(), evidenceRouter);
        this.inputBus = new DefaultInputBus(playerDataStore, checkManager, decisionEngine);
    }

    public void registerCheck(String id, CheckCategory category, Check check) {
        checkManager.register(id, category, check);
    }

    public boolean isCheckRegistered(String checkId) {
        return checkManager.isRegistered(checkId);
    }

    public void setCheckEnabled(String checkId, boolean enabled) {
        checkManager.setEnabled(checkId, enabled);
    }

    public boolean isCheckEnabled(String checkId) {
        return checkManager.isEnabled(checkId);
    }

    public Decision ingest(PlayerProfile profile, Snapshot snapshot) {
        return inputBus.publish(profile, snapshot);
    }

    public void markJoin(PlayerProfile profile) {
        inputBus.markJoin(profile);
    }

    public void markTeleport(PlayerProfile profile) {
        inputBus.markTeleport(profile);
    }

    public void markVelocity(PlayerProfile profile) {
        inputBus.markVelocity(profile);
    }

    public void setTicksPerSecond(PlayerProfile profile, double ticksPerSecond) {
        inputBus.setTicksPerSecond(profile, ticksPerSecond);
    }

    public void setPingEstimateMillis(PlayerProfile profile, int pingEstimateMillis) {
        inputBus.setPingEstimateMillis(profile, pingEstimateMillis);
    }

    public CoreDebugSnapshot debug(PlayerProfile profile, int evidenceLimit) {
        PlayerData playerData = playerDataStore.get(profile);
        if (playerData == null) {
            return null;
        }
        List<Evidence> recent = playerData.getAccumulator().snapshotRecentEvidence();
        int from = Math.max(0, recent.size() - evidenceLimit);
        List<Evidence> bounded = new ArrayList<Evidence>(recent.subList(from, recent.size()));
        return new CoreDebugSnapshot(
            playerData.getContextState().getPingEstimateMillis(),
            playerData.getContextState().getTicksPerSecond(),
            playerData.getContextState().getJoinTicks(),
            playerData.getContextState().getTeleportTicks(),
            playerData.getContextState().getVelocityTicks(),
            playerData.getPolicyState().snapshotCategoryTotals(),
            bounded
        );
    }

    public void subscribeDecisionEvents(DecisionEventListener listener) {
        decisionEventBus.subscribe(listener);
    }

    public void unsubscribeDecisionEvents(DecisionEventListener listener) {
        decisionEventBus.unsubscribe(listener);
    }

    public void releasePlayer(PlayerProfile profile) {
        inputBus.release(profile);
    }

    public void stop() {
        checkManager.stop();
    }
}
