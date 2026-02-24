package judexis.core.behavior.modules.packet;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class RotationPacketRepeatDeltaModule extends ConfiguredStatModule {
    public RotationPacketRepeatDeltaModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.LOOK, PacketType.Play.Client.POSITION,
                BufferChannel.ROTATION, BufferChannel.PACKET_INTERVAL, 24, 0.09D, 1.15D, 2.2D, 10.0D);
    }
}
