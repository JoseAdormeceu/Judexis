package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class VelocityReductionClusterModule extends ConfiguredStatModule {
    public VelocityReductionClusterModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Server.ENTITY_VELOCITY, PacketType.Play.Client.FLYING,
                BufferChannel.VELOCITY_REDUCTION, BufferChannel.HORIZONTAL, 18, 0.16D, 1.55D, 0.70D, 13.0D);
    }
}
