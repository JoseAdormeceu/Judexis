package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class GlideLowGravityModule extends ConfiguredStatModule {
    public GlideLowGravityModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION,
                BufferChannel.VERTICAL, BufferChannel.DISPLACEMENT_BURST, 18, 0.14D, 1.35D, 0.09D, 11.0D);
    }
}
