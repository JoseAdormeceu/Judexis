package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class NoKnockbackSimulationModule extends ConfiguredStatModule {
    public NoKnockbackSimulationModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Server.ENTITY_VELOCITY,
                BufferChannel.VELOCITY_REDUCTION, BufferChannel.DISPLACEMENT_BURST, 16, 0.20D, 1.7D, 0.75D, 14.0D);
    }
}
