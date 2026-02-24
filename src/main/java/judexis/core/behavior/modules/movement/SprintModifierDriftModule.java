package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class SprintModifierDriftModule extends ConfiguredStatModule {
    public SprintModifierDriftModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION,
                BufferChannel.HORIZONTAL, BufferChannel.PACKET_INTERVAL, 20, 0.18D, 1.55D, 0.42D, 10.0D);
    }
}
