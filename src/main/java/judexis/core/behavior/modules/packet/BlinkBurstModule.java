package judexis.core.behavior.modules.packet;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class BlinkBurstModule extends ConfiguredStatModule {
    public BlinkBurstModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION,
                BufferChannel.PACKET_INTERVAL, BufferChannel.DISPLACEMENT_BURST, 20, 0.11D, 1.3D, 170.0D, 14.0D);
    }
}
