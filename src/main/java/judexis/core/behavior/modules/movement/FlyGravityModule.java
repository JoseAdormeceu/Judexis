package judexis.core.behavior.modules.movement;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import judexis.core.behavior.BehavioralModule;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.player.runtime.PlayerData;
import judexis.core.player.runtime.PrimitiveRingBuffer;

public final class FlyGravityModule extends BehavioralModule {

    public FlyGravityModule(ModuleDescriptor descriptor) {
        super(descriptor, descriptor.cooldownTicks());
    }

    @Override
    protected void analyze(PacketEvent event, PlayerData data, long tick) {
        PacketType type = event.getPacketType();
        if (type != PacketType.Play.Client.FLYING && type != PacketType.Play.Client.POSITION) {
            return;
        }

        PrimitiveRingBuffer vertical = data.verticalSpeedBuffer();
        if (vertical.count() < 18) {
            return;
        }

        double current = vertical.valueAt(0);
        double previous = vertical.valueAt(1);
        double expected = Math.abs(previous - 0.08D) * 0.98D;
        double delta = Math.abs(current - expected);
        double cv = vertical.coefficientOfVariation();

        if (!data.isOnGround() && delta < 0.0065D && cv < 0.15D) {
            registerAnomaly();
            addSuspicion(13.0D);
        }
    }
}
