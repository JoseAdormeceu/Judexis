package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class SilentAimFovMismatchModule extends ConfiguredStatModule {
    public SilentAimFovMismatchModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.USE_ENTITY, PacketType.Play.Client.LOOK,
                BufferChannel.FOV_MISMATCH, BufferChannel.ROTATION, 16, 0.22D, 1.8D, 35.0D, 13.0D);
    }
}
