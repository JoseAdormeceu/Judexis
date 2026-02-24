package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class KillAuraGcdSnapModule extends ConfiguredStatModule {
    public KillAuraGcdSnapModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.LOOK, PacketType.Play.Client.USE_ENTITY,
                BufferChannel.YAW, BufferChannel.PITCH, 20, 0.11D, 1.4D, 4.0D, 11.0D);
    }
}
