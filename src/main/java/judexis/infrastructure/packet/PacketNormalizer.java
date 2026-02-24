package judexis.infrastructure.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import judexis.core.player.runtime.PlayerData;

public final class PacketNormalizer {

    public void normalize(PacketEvent event, PlayerData data) {
        PacketContainer packet = event.getPacket();
        PacketType type = event.getPacketType();
        long now = System.currentTimeMillis();

        if (type == PacketType.Play.Client.POSITION || type == PacketType.Play.Client.FLYING) {
            if (packet.getDoubles().size() >= 3) {
                double x = packet.getDoubles().read(0);
                double y = packet.getDoubles().read(1);
                double z = packet.getDoubles().read(2);
                boolean ground = packet.getBooleans().size() > 0 && packet.getBooleans().read(0);
                data.updatePosition(x, y, z, ground);

                double horizontal = data.horizontalSpeedBuffer().valueAt(0);
                double vertical = data.verticalSpeedBuffer().valueAt(0);
                double angleError = Math.abs(horizontal - vertical) * 8.0D;
                data.recordStrafeAngleError(angleError);
            }
        } else if (type == PacketType.Play.Client.LOOK) {
            if (packet.getFloat().size() >= 2) {
                float yaw = packet.getFloat().read(0);
                float pitch = packet.getFloat().read(1);
                data.updateRotation(yaw, pitch);
                data.recordFovMismatch(Math.abs(pitch) + data.rotationDeltaBuffer().valueAt(0));
            }
        } else if (type == PacketType.Play.Server.ENTITY_VELOCITY) {
            if (packet.getIntegers().size() >= 4 && packet.getIntegers().read(0) == event.getPlayer().getEntityId()) {
                double vx = packet.getIntegers().read(1) / 8000.0D;
                double vy = packet.getIntegers().read(2) / 8000.0D;
                double vz = packet.getIntegers().read(3) / 8000.0D;
                data.setVelocity(vx, vy, vz);
                double expectedHorizontal = Math.sqrt(vx * vx + vz * vz);
                double actualHorizontal = data.horizontalSpeedBuffer().valueAt(0);
                if (expectedHorizontal > 1.0E-5D) {
                    data.recordVelocityReductionRatio(actualHorizontal / expectedHorizontal);
                }
            }
        } else if (type == PacketType.Play.Client.ARM_ANIMATION) {
            data.recordAttack(Integer.MIN_VALUE, now);
        } else if (type == PacketType.Play.Client.BLOCK_PLACE) {
            data.recordPlacement(now);
        } else if (type == PacketType.Play.Client.BLOCK_DIG) {
            data.recordBreak(now);
        } else if (type == PacketType.Play.Client.USE_ENTITY) {
            int targetEntityId = packet.getIntegers().size() > 0 ? packet.getIntegers().read(0) : Integer.MIN_VALUE;
            data.recordAttack(targetEntityId, now);
            double horizontal = data.horizontalSpeedBuffer().valueAt(0);
            double yawDelta = data.yawDeltaBuffer().valueAt(0);
            double estimatedReach = 3.0D + (horizontal * 1.6D) + (yawDelta * 0.004D);
            data.recordReachDistance(estimatedReach);
        } else if (type == PacketType.Play.Client.TRANSACTION) {
            data.recordTransaction(now);
        }

        data.setTpsSnapshot(20.0D);
    }
}