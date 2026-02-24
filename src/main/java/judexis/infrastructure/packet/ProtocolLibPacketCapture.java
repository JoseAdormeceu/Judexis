package judexis.infrastructure.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import judexis.JudexisPlugin;
import judexis.core.behavior.BehavioralDetectionLayer;
import judexis.core.packet.PacketCaptureLayer;
import judexis.core.player.PlayerDataSnapshotLayer;
import judexis.core.player.PlayerSnapshot;
import judexis.core.player.runtime.PlayerData;

import java.util.UUID;

public final class ProtocolLibPacketCapture implements PacketCaptureLayer {

    private final JudexisPlugin plugin;
    private final PlayerDataSnapshotLayer playerDataLayer;
    private final BehavioralDetectionLayer behavioralLayer;
    private final ProtocolManager protocolManager;
    private final PacketNormalizer packetNormalizer;
    private PacketAdapter adapter;

    public ProtocolLibPacketCapture(JudexisPlugin plugin,
                                    PlayerDataSnapshotLayer playerDataLayer,
                                    BehavioralDetectionLayer behavioralLayer) {
        this.plugin = plugin;
        this.playerDataLayer = playerDataLayer;
        this.behavioralLayer = behavioralLayer;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.packetNormalizer = new PacketNormalizer();
    }

    @Override
    public void start() {
        this.adapter = new PacketAdapter(
                plugin,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.USE_ENTITY,
                PacketType.Play.Client.ARM_ANIMATION,
                PacketType.Play.Client.FLYING,
                PacketType.Play.Client.POSITION,
                PacketType.Play.Client.LOOK,
                PacketType.Play.Client.BLOCK_DIG,
                PacketType.Play.Client.BLOCK_PLACE,
                PacketType.Play.Server.ENTITY_VELOCITY,
                PacketType.Play.Client.TRANSACTION,
                PacketType.Play.Client.CUSTOM_PAYLOAD
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                route(event);
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                route(event);
            }
        };
        protocolManager.addPacketListener(adapter);
    }

    @Override
    public void stop() {
        if (adapter != null) {
            protocolManager.removePacketListener(adapter);
        }
    }

    @Override
    public void route(PacketEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        playerDataLayer.ensurePlayer(playerId, event.getPlayer().getName());
        PlayerSnapshot snapshot = playerDataLayer.getSnapshot(playerId);
        if (!(snapshot instanceof PlayerData)) {
            return;
        }

        PlayerData data = (PlayerData) snapshot;
        data.recordPacket(System.currentTimeMillis());
        packetNormalizer.normalize(event, data);
        behavioralLayer.handle(event, playerDataLayer);
    }
}
