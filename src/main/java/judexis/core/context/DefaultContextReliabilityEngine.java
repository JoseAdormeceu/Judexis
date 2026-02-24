package judexis.core.context;

import judexis.core.config.ConfigurationService;
import judexis.core.context.ContextReliabilityEngine;
import judexis.core.player.PlayerSnapshot;

public final class DefaultContextReliabilityEngine implements ContextReliabilityEngine {

    private final ConfigurationService config;

    public DefaultContextReliabilityEngine(ConfigurationService config) {
        this.config = config;
    }

    @Override
    public double reliability(PlayerSnapshot snapshot, long now) {
        double reliability = 1.0D;

        if (now - snapshot.getTeleportTimestamp() < config.teleportGraceMs()) {
            reliability *= 0.6D;
        }
        if (now - snapshot.getLastVelocityAppliedTimestamp() < config.velocityGraceMs()) {
            reliability *= 0.65D;
        }
        if (now - snapshot.getJoinTimestamp() < config.joinGraceMs()) {
            reliability *= 0.5D;
        }
        if (snapshot.getTpsSnapshot() < config.lowTpsThreshold()) {
            reliability *= 0.75D;
        }
        if (snapshot.getPing() > config.pingHighThreshold()) {
            reliability *= 0.7D;
        }
        if (snapshot.getPacketIrregularity() > config.packetIrregularityGrace()) {
            reliability *= 0.75D;
        }

        return clamp01(reliability);
    }

    private double clamp01(double value) {
        if (value < 0.0D) {
            return 0.0D;
        }
        if (value > 1.0D) {
            return 1.0D;
        }
        return value;
    }
}
