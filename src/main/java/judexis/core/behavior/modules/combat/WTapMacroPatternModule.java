package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class WTapMacroPatternModule extends ConfiguredStatModule {
    public WTapMacroPatternModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.USE_ENTITY,
                BufferChannel.ATTACK_INTERVAL, BufferChannel.STRAFE_ERROR, 20, 0.15D, 1.45D, 90.0D, 11.0D);
    }
}
