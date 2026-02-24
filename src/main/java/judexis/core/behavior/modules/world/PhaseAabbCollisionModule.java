package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class PhaseAabbCollisionModule extends ConfiguredStatModule {
    public PhaseAabbCollisionModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.POSITION, PacketType.Play.Client.FLYING,
                BufferChannel.DISPLACEMENT_BURST, BufferChannel.PACKET_INTERVAL, 18, 0.12D, 1.4D, 0.65D, 13.0D);
    }
}
