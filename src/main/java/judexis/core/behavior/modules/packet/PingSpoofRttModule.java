package judexis.core.behavior.modules.packet;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class PingSpoofRttModule extends ConfiguredStatModule {
    public PingSpoofRttModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.TRANSACTION, PacketType.Play.Client.FLYING,
                BufferChannel.TRANSACTION_INTERVAL, BufferChannel.PACKET_INTERVAL, 18, 0.13D, 1.45D, 250.0D, 12.0D);
    }
}
