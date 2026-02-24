package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import judexis.core.behavior.BehavioralModule;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.player.runtime.PlayerData;
import judexis.core.player.runtime.PrimitiveRingBuffer;

public final class SpeedFrictionModule extends BehavioralModule {

    public SpeedFrictionModule(ModuleDescriptor descriptor) {
        super(descriptor, descriptor.cooldownTicks());
    }

    @Override
    protected void analyze(PacketEvent event, PlayerData data, long tick) {
        PacketType type = event.getPacketType();
        if (type != PacketType.Play.Client.FLYING && type != PacketType.Play.Client.POSITION) {
            return;
        }

        PrimitiveRingBuffer horizontal = data.horizontalSpeedBuffer();
        if (horizontal.count() < 20) {
            return;
        }

        double current = horizontal.valueAt(0);
        double previous = horizontal.valueAt(1);
        double expected = previous * 0.91D;
        double frictionDelta = Math.abs(current - expected);
        double cv = horizontal.coefficientOfVariation();

        boolean sustainedDeviation = frictionDelta > 0.085D && cv < 0.32D;
        boolean unnaturalPersistence = horizontal.valueAt(0) > horizontal.valueAt(1) && horizontal.valueAt(1) > horizontal.valueAt(2);

        if (sustainedDeviation && unnaturalPersistence) {
            registerAnomaly();
            addSuspicion(12.0D);
        }
    }
}
