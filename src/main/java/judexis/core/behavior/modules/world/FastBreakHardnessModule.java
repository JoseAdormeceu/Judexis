package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class FastBreakHardnessModule extends ConfiguredStatModule {
    public FastBreakHardnessModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Client.ARM_ANIMATION,
                BufferChannel.BREAK_INTERVAL, BufferChannel.ROTATION, 20, 0.14D, 1.4D, 120.0D, 12.0D);
    }
}
