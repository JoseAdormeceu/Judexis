package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class ScaffoldAngleConsistencyModule extends ConfiguredStatModule {
    public ScaffoldAngleConsistencyModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.BLOCK_PLACE, PacketType.Play.Client.LOOK,
                BufferChannel.ROTATION, BufferChannel.PLACE_INTERVAL, 18, 0.12D, 1.30D, 2.8D, 10.0D);
    }
}
