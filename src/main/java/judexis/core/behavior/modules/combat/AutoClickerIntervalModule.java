package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class AutoClickerIntervalModule extends ConfiguredStatModule {
    public AutoClickerIntervalModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.ARM_ANIMATION, PacketType.Play.Client.USE_ENTITY,
                BufferChannel.ATTACK_INTERVAL, BufferChannel.ROTATION, 24, 0.10D, 1.25D, 85.0D, 12.0D);
    }
}
