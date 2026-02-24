package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class BlockReachWorldModule extends ConfiguredStatModule {
    public BlockReachWorldModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.BLOCK_PLACE, PacketType.Play.Client.BLOCK_DIG,
                BufferChannel.REACH_DISTANCE, BufferChannel.PLACE_INTERVAL, 16, 0.18D, 1.6D, 3.22D, 12.0D);
    }
}
