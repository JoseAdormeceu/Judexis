package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class CombatAimDistributionModule extends ConfiguredStatModule {
    public CombatAimDistributionModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.LOOK, PacketType.Play.Client.USE_ENTITY,
                BufferChannel.ROTATION, BufferChannel.YAW, 22, 0.13D, 1.30D, 3.5D, 10.0D);
    }
}
