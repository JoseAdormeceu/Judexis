package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class AirAccelerationModule extends ConfiguredStatModule {
    public AirAccelerationModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION,
                BufferChannel.HORIZONTAL, BufferChannel.DISPLACEMENT_BURST, 20, 0.17D, 1.52D, 0.44D, 11.0D);
    }
}
