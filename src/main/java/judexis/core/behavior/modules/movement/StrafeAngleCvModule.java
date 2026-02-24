package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class StrafeAngleCvModule extends ConfiguredStatModule {
    public StrafeAngleCvModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION,
                BufferChannel.STRAFE_ERROR, BufferChannel.HORIZONTAL, 18, 0.12D, 1.4D, 8.0D, 10.0D);
    }
}
