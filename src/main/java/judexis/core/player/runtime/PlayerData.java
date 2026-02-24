package judexis.core.player.runtime;

import judexis.core.player.PlayerSnapshot;

import java.util.UUID;

public final class PlayerData implements PlayerSnapshot {

    private final UUID playerId;
    private final String playerName;
    private final long joinTimestamp;

    private final PrimitiveRingBuffer horizontalSpeedBuffer;
    private final PrimitiveRingBuffer verticalSpeedBuffer;
    private final PrimitiveRingBuffer rotationDeltaBuffer;
    private final PrimitiveRingBuffer yawDeltaBuffer;
    private final PrimitiveRingBuffer pitchDeltaBuffer;
    private final PrimitiveRingBuffer attackIntervalBuffer;
    private final PrimitiveRingBuffer placementIntervalBuffer;
    private final PrimitiveRingBuffer breakIntervalBuffer;
    private final PrimitiveRingBuffer transactionIntervalBuffer;
    private final PrimitiveRingBuffer packetIntervalBuffer;
    private final PrimitiveRingBuffer reachDistanceBuffer;
    private final PrimitiveRingBuffer velocityReductionBuffer;
    private final PrimitiveRingBuffer strafeAngleErrorBuffer;
    private final PrimitiveRingBuffer displacementBurstBuffer;
    private final PrimitiveRingBuffer targetSwitchIntervalBuffer;
    private final PrimitiveRingBuffer fovMismatchBuffer;

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private boolean onGround;
    private int ping;
    private long teleportTimestamp;
    private long lastVelocityAppliedTimestamp;
    private double tpsSnapshot;
    private int packetIrregularity;
    private long lastAttackAt;
    private long lastPlaceAt;
    private long lastBreakAt;
    private long lastTransactionAt;
    private long lastPacketAt;
    private long lastAttackIntervalMs;
    private long lastPlacementIntervalMs;
    private long lastBreakIntervalMs;
    private long lastTransactionIntervalMs;
    private int lastTargetEntityId;
    private long lastTargetSwitchAt;

    public PlayerData(UUID playerId, String playerName, int ringSize) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.joinTimestamp = System.currentTimeMillis();
        this.teleportTimestamp = 0L;
        this.lastVelocityAppliedTimestamp = 0L;
        this.horizontalSpeedBuffer = new PrimitiveRingBuffer(ringSize);
        this.verticalSpeedBuffer = new PrimitiveRingBuffer(ringSize);
        this.rotationDeltaBuffer = new PrimitiveRingBuffer(ringSize);
        this.yawDeltaBuffer = new PrimitiveRingBuffer(ringSize);
        this.pitchDeltaBuffer = new PrimitiveRingBuffer(ringSize);
        this.attackIntervalBuffer = new PrimitiveRingBuffer(ringSize);
        this.placementIntervalBuffer = new PrimitiveRingBuffer(ringSize);
        this.breakIntervalBuffer = new PrimitiveRingBuffer(ringSize);
        this.transactionIntervalBuffer = new PrimitiveRingBuffer(ringSize);
        this.packetIntervalBuffer = new PrimitiveRingBuffer(ringSize);
        this.reachDistanceBuffer = new PrimitiveRingBuffer(ringSize);
        this.velocityReductionBuffer = new PrimitiveRingBuffer(ringSize);
        this.strafeAngleErrorBuffer = new PrimitiveRingBuffer(ringSize);
        this.displacementBurstBuffer = new PrimitiveRingBuffer(ringSize);
        this.targetSwitchIntervalBuffer = new PrimitiveRingBuffer(ringSize);
        this.fovMismatchBuffer = new PrimitiveRingBuffer(ringSize);
        this.lastTargetEntityId = Integer.MIN_VALUE;
    }

    public UUID playerId() {
        return playerId;
    }

    public String playerName() {
        return playerName;
    }

    public PrimitiveRingBuffer horizontalSpeedBuffer() {
        return horizontalSpeedBuffer;
    }

    public PrimitiveRingBuffer verticalSpeedBuffer() {
        return verticalSpeedBuffer;
    }

    public PrimitiveRingBuffer rotationDeltaBuffer() {
        return rotationDeltaBuffer;
    }

    public PrimitiveRingBuffer attackIntervalBuffer() {
        return attackIntervalBuffer;
    }

    public PrimitiveRingBuffer yawDeltaBuffer() {
        return yawDeltaBuffer;
    }

    public PrimitiveRingBuffer pitchDeltaBuffer() {
        return pitchDeltaBuffer;
    }

    public PrimitiveRingBuffer placementIntervalBuffer() {
        return placementIntervalBuffer;
    }

    public PrimitiveRingBuffer breakIntervalBuffer() {
        return breakIntervalBuffer;
    }

    public PrimitiveRingBuffer transactionIntervalBuffer() {
        return transactionIntervalBuffer;
    }

    public PrimitiveRingBuffer packetIntervalBuffer() {
        return packetIntervalBuffer;
    }

    public PrimitiveRingBuffer reachDistanceBuffer() {
        return reachDistanceBuffer;
    }

    public PrimitiveRingBuffer velocityReductionBuffer() {
        return velocityReductionBuffer;
    }

    public PrimitiveRingBuffer strafeAngleErrorBuffer() {
        return strafeAngleErrorBuffer;
    }

    public PrimitiveRingBuffer displacementBurstBuffer() {
        return displacementBurstBuffer;
    }

    public PrimitiveRingBuffer targetSwitchIntervalBuffer() {
        return targetSwitchIntervalBuffer;
    }

    public PrimitiveRingBuffer fovMismatchBuffer() {
        return fovMismatchBuffer;
    }

    public void updatePosition(double x, double y, double z, boolean onGround) {
        double dx = x - this.x;
        double dz = z - this.z;
        double dy = y - this.y;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        this.horizontalSpeedBuffer.push(horizontal);
        this.verticalSpeedBuffer.push(Math.abs(dy));
        this.displacementBurstBuffer.push(Math.sqrt(dx * dx + dy * dy + dz * dz));
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }

    public void updateRotation(float yaw, float pitch) {
        float deltaYaw = Math.abs(yaw - this.yaw);
        float deltaPitch = Math.abs(pitch - this.pitch);
        this.rotationDeltaBuffer.push(deltaYaw + deltaPitch);
        this.yawDeltaBuffer.push(deltaYaw);
        this.pitchDeltaBuffer.push(deltaPitch);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void recordPacket(long now) {
        if (lastPacketAt > 0L) {
            long interval = now - lastPacketAt;
            packetIntervalBuffer.push(interval);
            if (interval > 160L) {
                incrementPacketIrregularity();
            } else {
                decayPacketIrregularity();
            }
        }
        lastPacketAt = now;
    }

    public void recordAttack(int targetEntityId, long now) {
        if (lastAttackAt > 0L) {
            lastAttackIntervalMs = now - lastAttackAt;
            attackIntervalBuffer.push(lastAttackIntervalMs);
        }
        if (lastTargetEntityId != Integer.MIN_VALUE && lastTargetEntityId != targetEntityId && lastTargetSwitchAt > 0L) {
            targetSwitchIntervalBuffer.push(now - lastTargetSwitchAt);
        }
        lastTargetEntityId = targetEntityId;
        lastTargetSwitchAt = now;
        lastAttackAt = now;
    }

    public void recordPlacement(long now) {
        if (lastPlaceAt > 0L) {
            lastPlacementIntervalMs = now - lastPlaceAt;
            placementIntervalBuffer.push(lastPlacementIntervalMs);
        }
        lastPlaceAt = now;
    }

    public void recordBreak(long now) {
        if (lastBreakAt > 0L) {
            lastBreakIntervalMs = now - lastBreakAt;
            breakIntervalBuffer.push(lastBreakIntervalMs);
        }
        lastBreakAt = now;
    }

    public void recordTransaction(long now) {
        if (lastTransactionAt > 0L) {
            lastTransactionIntervalMs = now - lastTransactionAt;
            transactionIntervalBuffer.push(lastTransactionIntervalMs);
        }
        lastTransactionAt = now;
    }

    public void recordReachDistance(double distance) {
        reachDistanceBuffer.push(distance);
    }

    public void recordVelocityReductionRatio(double ratio) {
        velocityReductionBuffer.push(ratio);
    }

    public void recordStrafeAngleError(double error) {
        strafeAngleErrorBuffer.push(error);
    }

    public void recordFovMismatch(double mismatch) {
        fovMismatchBuffer.push(mismatch);
    }

    public void setVelocity(double velocityX, double velocityY, double velocityZ) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.lastVelocityAppliedTimestamp = System.currentTimeMillis();
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public void setTeleportTimestamp(long teleportTimestamp) {
        this.teleportTimestamp = teleportTimestamp;
    }

    public void setTpsSnapshot(double tpsSnapshot) {
        this.tpsSnapshot = tpsSnapshot;
    }

    public int packetIrregularity() {
        return packetIrregularity;
    }

    public void incrementPacketIrregularity() {
        if (packetIrregularity < 1000) {
            packetIrregularity++;
        }
    }

    public void decayPacketIrregularity() {
        if (packetIrregularity > 0) {
            packetIrregularity--;
        }
    }

    @Override
    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public double getVelocityX() {
        return velocityX;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }

    @Override
    public double getVelocityZ() {
        return velocityZ;
    }

    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public int getPing() {
        return ping;
    }

    @Override
    public double getTpsSnapshot() {
        return tpsSnapshot;
    }

    @Override
    public long getJoinTimestamp() {
        return joinTimestamp;
    }

    @Override
    public long getTeleportTimestamp() {
        return teleportTimestamp;
    }

    @Override
    public long getLastVelocityAppliedTimestamp() {
        return lastVelocityAppliedTimestamp;
    }

    @Override
    public int getPacketIrregularity() {
        return packetIrregularity;
    }

    @Override
    public long getLastAttackIntervalMs() {
        return lastAttackIntervalMs;
    }

    @Override
    public long getLastPlacementIntervalMs() {
        return lastPlacementIntervalMs;
    }

    @Override
    public long getLastBreakIntervalMs() {
        return lastBreakIntervalMs;
    }

    @Override
    public long getLastTransactionIntervalMs() {
        return lastTransactionIntervalMs;
    }
}
