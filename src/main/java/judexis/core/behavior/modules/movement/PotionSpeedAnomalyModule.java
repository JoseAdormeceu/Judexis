package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class PotionSpeedAnomalyModule extends ConfiguredStatModule {
    public PotionSpeedAnomalyModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION,
                BufferChannel.HORIZONTAL, BufferChannel.VERTICAL, 18, 0.19D, 1.6D, 0.46D, 10.0D);
    }
}
