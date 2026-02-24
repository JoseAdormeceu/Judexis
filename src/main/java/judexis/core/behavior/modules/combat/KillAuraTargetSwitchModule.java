package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class KillAuraTargetSwitchModule extends ConfiguredStatModule {
    public KillAuraTargetSwitchModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.USE_ENTITY, PacketType.Play.Client.ARM_ANIMATION,
                BufferChannel.TARGET_SWITCH_INTERVAL, BufferChannel.ATTACK_INTERVAL, 12, 0.18D, 1.6D, 130.0D, 12.0D);
    }
}
