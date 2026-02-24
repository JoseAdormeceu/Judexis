package judexis.core.player;

import java.util.UUID;

public interface PlayerSnapshot {
    UUID getPlayerId();
    String getPlayerName();
    double getX();
    double getY();
    double getZ();
    float getYaw();
    float getPitch();
    double getVelocityX();
    double getVelocityY();
    double getVelocityZ();
    boolean isOnGround();
    int getPing();
    double getTpsSnapshot();
    long getJoinTimestamp();
    long getTeleportTimestamp();
    long getLastVelocityAppliedTimestamp();
    int getPacketIrregularity();
    long getLastAttackIntervalMs();
    long getLastPlacementIntervalMs();
    long getLastBreakIntervalMs();
    long getLastTransactionIntervalMs();
}
