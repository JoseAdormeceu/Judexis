package io.judexis.core.check;

import io.judexis.core.snapshot.Snapshot;

/**
 * Base anti-cheat check contract.
 */
public interface Check {
    /**
     * Called once when the check is registered.
     */
    void onLoad();

    /**
     * Called for each incoming snapshot.
     *
     * @param context runtime view used for reading context and emitting evidence
     * @param snapshot immutable snapshot payload
     */
    void onSnapshot(CheckContext context, Snapshot snapshot);

    /**
     * Called once when the check is removed.
     */
    void onUnload();

    /**
     * @return unique check name
     */
    String getName();
}
