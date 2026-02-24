package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class KillAuraMicroCorrectionModule extends ConfiguredStatModule {
    public KillAuraMicroCorrectionModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.LOOK, PacketType.Play.Client.ARM_ANIMATION,
                BufferChannel.ROTATION, BufferChannel.ATTACK_INTERVAL, 20, 0.14D, 1.35D, 3.0D, 10.0D);
    }
}
