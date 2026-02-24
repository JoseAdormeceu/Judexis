package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class JesusLiquidWalkModule extends ConfiguredStatModule {
    public JesusLiquidWalkModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION,
                BufferChannel.VERTICAL, BufferChannel.HORIZONTAL, 20, 0.10D, 1.25D, 0.03D, 12.0D);
    }
}
