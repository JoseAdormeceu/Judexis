package io.judexis.core.pipeline;

import io.judexis.core.check.CheckManager;
import io.judexis.core.context.ContextState;
import io.judexis.core.data.PlayerData;
import io.judexis.core.data.PlayerDataStore;
import io.judexis.core.decision.Decision;
import io.judexis.core.decision.DecisionEngine;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.physics.MotionState;
import io.judexis.core.physics.PhysicsEngine;
import io.judexis.core.physics.PredictionResult;
import io.judexis.core.physics.analysis.ErrorMeasurement;
import io.judexis.core.physics.analysis.PredictionComparator;
import io.judexis.core.snapshot.MovementSnapshot;
import io.judexis.core.snapshot.NetworkSnapshot;
import io.judexis.core.snapshot.Snapshot;
import io.judexis.core.snapshot.WorldSnapshot;

/**
 * Default in-memory input bus for low-latency dispatch.
 */
public final class DefaultInputBus implements InputBus {
    private final PlayerDataStore playerDataStore;
    private final CheckManager checkManager;
    private final DecisionEngine decisionEngine;
    private final PhysicsEngine physicsEngine;
    private final PredictionComparator predictionComparator;

    public DefaultInputBus(PlayerDataStore playerDataStore, CheckManager checkManager, DecisionEngine decisionEngine) {
        this.playerDataStore = playerDataStore;
        this.checkManager = checkManager;
        this.decisionEngine = decisionEngine;
        this.physicsEngine = new PhysicsEngine();
        this.predictionComparator = new PredictionComparator();
    }

    public Decision publish(PlayerProfile profile, Snapshot snapshot) {
        PlayerData playerData = playerDataStore.getOrCreate(profile);
        ContextState state = playerData.getContextState();
        state.advanceTick();

        if (snapshot instanceof NetworkSnapshot) {
            state.setPingEstimateMillis(((NetworkSnapshot) snapshot).getEstimatedPingMillis());
        }
        if (snapshot instanceof WorldSnapshot) {
            String worldId = ((WorldSnapshot) snapshot).getWorldId();
            String previous = playerData.getCurrentWorldId();
            if (previous == null || !previous.equals(worldId)) {
                playerData.setCurrentWorldId(worldId);
                state.markWorldChange();
            }
        }
        if (snapshot instanceof MovementSnapshot) {
            processMovementSnapshot((MovementSnapshot) snapshot, playerData, state);
        }

        checkManager.dispatch(profile, playerData, snapshot);
        return decisionEngine.evaluate(profile, state, playerData.getAccumulator());
    }

    private void processMovementSnapshot(MovementSnapshot movementSnapshot,
                                         PlayerData playerData,
                                         ContextState contextState) {
        PredictionResult pending = playerData.getPendingPredictionResult();
        MotionState pendingState = playerData.getPendingPredictedState();

        playerData.getErrorAccumulator().tickDecay();

        if (pending != null && pendingState != null) {
            ErrorMeasurement measurement = predictionComparator.computeError(movementSnapshot, pending, contextState);
            playerData.setLatestErrorMeasurement(measurement);
            playerData.getMotionHistoryBuffer().add(movementSnapshot, pendingState, pending, measurement);
            playerData.getErrorAccumulator().addMeasurement(measurement);
        }

        MotionState observed = buildObservedState(movementSnapshot, playerData.getLastObservedMotionState());
        PredictionResult nextPrediction = physicsEngine.simulateNextTick(observed);
        playerData.setLastObservedMotionState(observed);
        playerData.setPendingPredictedState(nextPrediction.getPredictedState());
        playerData.setPendingPredictionResult(nextPrediction);
    }

    private MotionState buildObservedState(MovementSnapshot movementSnapshot, MotionState previousObserved) {
        int airTicks = 0;
        int groundTicks = 0;
        if (previousObserved != null) {
            if (movementSnapshot.isOnGround()) {
                groundTicks = previousObserved.getGroundTicks() + 1;
                airTicks = 0;
            } else {
                airTicks = previousObserved.getAirTicks() + 1;
                groundTicks = 0;
            }
        } else {
            if (movementSnapshot.isOnGround()) {
                groundTicks = 1;
            } else {
                airTicks = 1;
            }
        }

        return new MotionState(
            movementSnapshot.getX(),
            movementSnapshot.getY(),
            movementSnapshot.getZ(),
            movementSnapshot.deltaX(),
            movementSnapshot.deltaY(),
            movementSnapshot.deltaZ(),
            movementSnapshot.isOnGround(),
            false,
            false,
            false,
            airTicks,
            groundTicks
        );
    }

    public void markJoin(PlayerProfile profile) {
        playerDataStore.getOrCreate(profile).getContextState().markJoin();
    }

    public void markTeleport(PlayerProfile profile) {
        playerDataStore.getOrCreate(profile).getContextState().markTeleport();
    }

    public void markVelocity(PlayerProfile profile) {
        playerDataStore.getOrCreate(profile).getContextState().markVelocity();
    }

    public void setTicksPerSecond(PlayerProfile profile, double ticksPerSecond) {
        playerDataStore.getOrCreate(profile).getContextState().setTicksPerSecond(ticksPerSecond);
    }

    public void setPingEstimateMillis(PlayerProfile profile, int pingMillis) {
        playerDataStore.getOrCreate(profile).getContextState().setPingEstimateMillis(pingMillis);
    }

    public void release(PlayerProfile profile) {
        playerDataStore.remove(profile);
    }
}
