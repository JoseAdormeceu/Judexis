package io.judexis.core.snapshot;

/**
 * Base contract for all immutable snapshots flowing through the core pipeline.
 */
public interface Snapshot {
    /**
     * @return logical server tick where the snapshot was captured
     */
    long getTick();

    /**
     * @return monotonic capture time in nanoseconds
     */
    long getCapturedAtNanos();

    /**
     * @return snapshot channel type
     */
    SnapshotType getType();
}
