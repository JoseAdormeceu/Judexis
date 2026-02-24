package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class WorldOrderAnomalyModule extends ConfiguredStatModule {
    public WorldOrderAnomalyModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Client.BLOCK_PLACE,
                BufferChannel.TRANSACTION_INTERVAL, BufferChannel.BREAK_INTERVAL, 20, 0.14D, 1.5D, 120.0D, 10.0D);
    }
}
