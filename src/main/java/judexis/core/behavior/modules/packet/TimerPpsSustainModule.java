package judexis.core.behavior.modules.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import judexis.core.behavior.BehavioralModule;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.player.runtime.PlayerData;
import judexis.core.player.runtime.PrimitiveRingBuffer;

public final class TimerPpsSustainModule extends BehavioralModule {

    public TimerPpsSustainModule(ModuleDescriptor descriptor) {
        super(descriptor, descriptor.cooldownTicks());
    }

    @Override
    protected void analyze(PacketEvent event, PlayerData data, long tick) {
        PacketType type = event.getPacketType();
        if (type != PacketType.Play.Client.FLYING && type != PacketType.Play.Client.POSITION) {
            return;
        }

        PrimitiveRingBuffer packetIntervals = data.packetIntervalBuffer();
        if (packetIntervals.count() < 30) {
            return;
        }

        double mean = packetIntervals.mean();
        double cv = packetIntervals.coefficientOfVariation();
        double entropy = packetIntervals.entropy8();

        if (mean < 45.0D && cv < 0.08D && entropy < 1.30D) {
            registerAnomaly();
            addSuspicion(14.0D);
        }
    }
}
