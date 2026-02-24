package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class NukerBurstModule extends ConfiguredStatModule {
    public NukerBurstModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Client.POSITION,
                BufferChannel.BREAK_INTERVAL, BufferChannel.DISPLACEMENT_BURST, 20, 0.12D, 1.35D, 95.0D, 13.0D);
    }
}
