package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class NoFallGroundSpoofModule extends ConfiguredStatModule {
    public NoFallGroundSpoofModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION,
                BufferChannel.VERTICAL, BufferChannel.PACKET_INTERVAL, 20, 0.20D, 1.7D, 0.60D, 12.0D);
    }
}
