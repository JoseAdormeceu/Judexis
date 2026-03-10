package io.judexis.core.snapshot;

/**
 * Network quality snapshot used by checks to contextualize packet timing and congestion.
 */
public final class NetworkSnapshot implements Snapshot {
    private final long tick;
    private final long capturedAtNanos;
    private final int estimatedPingMillis;
    private final int packetRatePerSecond;
    private final int inboundQueueDepth;
    private final int outboundQueueDepth;

    public NetworkSnapshot(long tick, long capturedAtNanos, int estimatedPingMillis,
                           int packetRatePerSecond, int inboundQueueDepth, int outboundQueueDepth) {
        this.tick = tick;
        this.capturedAtNanos = capturedAtNanos;
        this.estimatedPingMillis = estimatedPingMillis;
        this.packetRatePerSecond = packetRatePerSecond;
        this.inboundQueueDepth = inboundQueueDepth;
        this.outboundQueueDepth = outboundQueueDepth;
    }

    public long getTick() {
        return tick;
    }

    public long getCapturedAtNanos() {
        return capturedAtNanos;
    }

    public SnapshotType getType() {
        return SnapshotType.NETWORK;
    }

    public int getEstimatedPingMillis() {
        return estimatedPingMillis;
    }

    public int getPacketRatePerSecond() {
        return packetRatePerSecond;
    }

    public int getInboundQueueDepth() {
        return inboundQueueDepth;
    }

    public int getOutboundQueueDepth() {
        return outboundQueueDepth;
    }
}
