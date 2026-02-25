package io.judexis.core.pipeline;

import io.judexis.core.decision.Decision;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.snapshot.Snapshot;

/**
 * Internal event bus that dispatches snapshots through core runtime.
 */
public interface InputBus {
    Decision publish(PlayerProfile profile, Snapshot snapshot);

    void markJoin(PlayerProfile profile);

    void markTeleport(PlayerProfile profile);

    void markVelocity(PlayerProfile profile);

    void setTicksPerSecond(PlayerProfile profile, double ticksPerSecond);

    void setPingEstimateMillis(PlayerProfile profile, int pingMillis);

    void release(PlayerProfile profile);
}
