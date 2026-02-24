package judexis.core.behavior;

import com.comphenix.protocol.events.PacketEvent;
import judexis.core.player.PlayerDataSnapshotLayer;

public interface BehavioralDetectionLayer {
    void handle(PacketEvent event, PlayerDataSnapshotLayer snapshotLayer);
    void tick(long currentTick);
}
