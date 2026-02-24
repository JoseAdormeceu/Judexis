package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import judexis.core.behavior.BehavioralModule;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.player.runtime.PlayerData;
import judexis.core.player.runtime.PrimitiveRingBuffer;

public final class ReachRaytraceModule extends BehavioralModule {

    public ReachRaytraceModule(ModuleDescriptor descriptor) {
        super(descriptor, descriptor.cooldownTicks());
    }

    @Override
    protected void analyze(PacketEvent event, PlayerData data, long tick) {
        if (event.getPacketType() != PacketType.Play.Client.USE_ENTITY) {
            return;
        }

        PrimitiveRingBuffer reach = data.reachDistanceBuffer();
        if (reach.count() < 18) {
            return;
        }

        double p95 = reach.percentile95();
        double variance = reach.variance();
        double cv = reach.coefficientOfVariation();

        boolean sustainedLongReach = p95 > 3.17D;
        boolean clustered = variance < 0.0025D && cv < 0.08D;

        if (sustainedLongReach && clustered) {
            registerAnomaly();
            addSuspicion(15.0D);
        }
    }
}
