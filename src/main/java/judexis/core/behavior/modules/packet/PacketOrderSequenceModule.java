package judexis.core.behavior.modules.packet;

import com.comphenix.protocol.PacketType;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.base.BufferChannel;
import judexis.core.behavior.modules.base.ConfiguredStatModule;

public final class PacketOrderSequenceModule extends ConfiguredStatModule {
    public PacketOrderSequenceModule(ModuleDescriptor descriptor) {
        super(descriptor, PacketType.Play.Client.TRANSACTION, PacketType.Play.Client.CUSTOM_PAYLOAD,
                BufferChannel.PACKET_INTERVAL, BufferChannel.TRANSACTION_INTERVAL, 22, 0.12D, 1.4D, 180.0D, 11.0D);
    }
}
