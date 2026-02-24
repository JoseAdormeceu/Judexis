package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class MovementTimerDesyncModule extends ConfiguredStatModule {
    public MovementTimerDesyncModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION,
                BufferChannel.PACKET_INTERVAL, BufferChannel.HORIZONTAL, 28, 0.09D, 1.25D, 46.0D, 13.0D);
    }
}
