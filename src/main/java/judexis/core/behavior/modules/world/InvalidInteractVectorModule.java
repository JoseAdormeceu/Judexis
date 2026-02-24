package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class InvalidInteractVectorModule extends ConfiguredStatModule {
    public InvalidInteractVectorModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.BLOCK_PLACE, PacketType.Play.Client.LOOK,
                BufferChannel.FOV_MISMATCH, BufferChannel.ROTATION, 18, 0.16D, 1.55D, 38.0D, 11.0D);
    }
}
