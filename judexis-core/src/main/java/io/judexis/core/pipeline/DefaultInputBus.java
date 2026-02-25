package io.judexis.core.pipeline;

import io.judexis.core.check.CheckManager;
import io.judexis.core.context.ContextState;
import io.judexis.core.data.PlayerData;
import io.judexis.core.data.PlayerDataStore;
import io.judexis.core.decision.Decision;
import io.judexis.core.decision.DecisionEngine;
import io.judexis.core.domain.PlayerProfile;
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

    public DefaultInputBus(PlayerDataStore playerDataStore, CheckManager checkManager, DecisionEngine decisionEngine) {
        this.playerDataStore = playerDataStore;
        this.checkManager = checkManager;
        this.decisionEngine = decisionEngine;
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

        checkManager.dispatch(profile, playerData, snapshot);
        return decisionEngine.evaluate(profile, state, playerData.getAccumulator());
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
