package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class TriggerBotReactionModule extends ConfiguredStatModule {
    public TriggerBotReactionModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.USE_ENTITY, PacketType.Play.Client.ARM_ANIMATION,
                BufferChannel.ATTACK_INTERVAL, BufferChannel.TARGET_SWITCH_INTERVAL, 20, 0.14D, 1.35D, 95.0D, 11.0D);
    }
}
