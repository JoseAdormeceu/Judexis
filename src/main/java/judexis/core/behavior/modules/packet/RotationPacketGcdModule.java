package judexis.core.behavior.modules.packet;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class RotationPacketGcdModule extends ConfiguredStatModule {
    public RotationPacketGcdModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.LOOK, PacketType.Play.Client.FLYING,
                BufferChannel.YAW, BufferChannel.PITCH, 24, 0.10D, 1.2D, 1.8D, 10.0D);
    }
}
