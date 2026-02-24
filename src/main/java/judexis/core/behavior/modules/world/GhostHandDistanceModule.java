package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class GhostHandDistanceModule extends ConfiguredStatModule {
    public GhostHandDistanceModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Client.BLOCK_PLACE,
                BufferChannel.REACH_DISTANCE, BufferChannel.ROTATION, 14, 0.18D, 1.6D, 3.35D, 14.0D);
    }
}
