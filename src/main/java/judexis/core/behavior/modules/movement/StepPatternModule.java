package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class StepPatternModule extends ConfiguredStatModule {
    public StepPatternModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.POSITION, PacketType.Play.Client.FLYING,
                BufferChannel.VERTICAL, BufferChannel.PACKET_INTERVAL, 16, 0.13D, 1.45D, 0.65D, 10.0D);
    }
}
