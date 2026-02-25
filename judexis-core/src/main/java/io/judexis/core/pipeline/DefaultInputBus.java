package io.judexis.core.pipeline;

import io.judexis.core.check.Check;
import io.judexis.core.check.CheckContext;
import io.judexis.core.context.ContextRegistry;
import io.judexis.core.context.ContextState;
import io.judexis.core.context.PlayerRuntimeContext;
import io.judexis.core.decision.Decision;
import io.judexis.core.decision.DecisionEngine;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.snapshot.NetworkSnapshot;
import io.judexis.core.snapshot.Snapshot;
import io.judexis.core.snapshot.WorldSnapshot;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Default in-memory input bus for low-latency dispatch.
 */
public final class DefaultInputBus implements InputBus {
    private final List<Check> checks;
    private final ContextRegistry contextRegistry;
    private final DecisionEngine decisionEngine;

    public DefaultInputBus(ContextRegistry contextRegistry, DecisionEngine decisionEngine) {
        if (contextRegistry == null) {
            throw new IllegalArgumentException("contextRegistry is required");
        }
        if (decisionEngine == null) {
            throw new IllegalArgumentException("decisionEngine is required");
        }
        this.checks = new CopyOnWriteArrayList<Check>();
        this.contextRegistry = contextRegistry;
        this.decisionEngine = decisionEngine;
    }

    public void register(Check check) {
        if (check == null) {
            throw new IllegalArgumentException("check is required");
        }
        checks.add(check);
        check.onLoad();
    }

    public void unregister(Check check) {
        if (check == null) {
            throw new IllegalArgumentException("check is required");
        }
        if (checks.remove(check)) {
            check.onUnload();
        }
    }

    public Decision publish(PlayerProfile profile, Snapshot snapshot) {
        if (profile == null) {
            throw new IllegalArgumentException("profile is required");
        }
        if (snapshot == null) {
            throw new IllegalArgumentException("snapshot is required");
        }
        PlayerRuntimeContext runtimeContext = contextRegistry.getOrCreate(profile);
        ContextState state = runtimeContext.getState();
        updateContext(state, runtimeContext, snapshot);

        CheckContext checkContext = new CheckContext(profile, state, runtimeContext.getViolationAccumulator());
        for (int i = 0; i < checks.size(); i++) {
            checks.get(i).onSnapshot(checkContext, snapshot);
        }
        return decisionEngine.evaluate(profile, state, runtimeContext.getViolationAccumulator());
    }

    public void release(PlayerProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("profile is required");
        }
        contextRegistry.remove(profile);
    }

    private void updateContext(ContextState state, PlayerRuntimeContext runtimeContext, Snapshot snapshot) {
        state.advanceTick();
        if (snapshot instanceof NetworkSnapshot) {
            NetworkSnapshot networkSnapshot = (NetworkSnapshot) snapshot;
            state.setPingEstimateMillis(networkSnapshot.getEstimatedPingMillis());
        } else if (snapshot instanceof WorldSnapshot) {
            WorldSnapshot worldSnapshot = (WorldSnapshot) snapshot;
            String worldId = worldSnapshot.getWorldId();
            String previousWorldId = runtimeContext.getCurrentWorldId();
            if (previousWorldId == null || !previousWorldId.equals(worldId)) {
                runtimeContext.setCurrentWorldId(worldId);
                state.markWorldChange();
            }
        }
    }
}
