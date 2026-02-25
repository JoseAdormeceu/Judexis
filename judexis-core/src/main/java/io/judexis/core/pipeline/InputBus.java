package io.judexis.core.pipeline;

import io.judexis.core.check.Check;
import io.judexis.core.decision.Decision;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.snapshot.Snapshot;

/**
 * Internal event bus that dispatches snapshots into loaded checks.
 */
public interface InputBus {
    /**
     * Registers a check and invokes its load lifecycle hook.
     *
     * @param check check instance
     */
    void register(Check check);

    /**
     * Unregisters a check and invokes its unload lifecycle hook.
     *
     * @param check check instance
     */
    void unregister(Check check);

    /**
     * Dispatches one snapshot through all active checks and evaluates decision output.
     *
     * @param profile player identity
     * @param snapshot snapshot payload
     * @return latest decision returned by the decision engine
     */
    Decision publish(PlayerProfile profile, Snapshot snapshot);

    /**
     * Marks a player join transition in context state.
     *
     * @param profile player identity
     */
    void markJoin(PlayerProfile profile);

    /**
     * Marks a player teleport transition in context state.
     *
     * @param profile player identity
     */
    void markTeleport(PlayerProfile profile);

    /**
     * Updates tick-rate estimate for the player context.
     *
     * @param profile player identity
     * @param ticksPerSecond observed TPS
     */
    void setTicksPerSecond(PlayerProfile profile, double ticksPerSecond);

    /**
     * Updates ping estimate for the player context.
     *
     * @param profile player identity
     * @param pingMillis ping estimate in milliseconds
     */
    void setPingEstimateMillis(PlayerProfile profile, int pingMillis);

    /**
     * Clears runtime state for a player.
     *
     * @param profile player identity
     */
    void release(PlayerProfile profile);
}
