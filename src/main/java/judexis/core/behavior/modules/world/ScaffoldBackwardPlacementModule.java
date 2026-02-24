package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class ScaffoldBackwardPlacementModule extends ConfiguredStatModule {
    public ScaffoldBackwardPlacementModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.BLOCK_PLACE, PacketType.Play.Client.FLYING,
                BufferChannel.PLACE_INTERVAL, BufferChannel.YAW, 18, 0.14D, 1.45D, 95.0D, 11.0D);
    }
}
