package judexis.core.behavior.modules.world;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import judexis.core.behavior.BehavioralModule;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.player.runtime.PlayerData;
import judexis.core.player.runtime.PrimitiveRingBuffer;

public final class ScaffoldIntervalUniformityModule extends BehavioralModule {

    public ScaffoldIntervalUniformityModule(ModuleDescriptor descriptor) {
        super(descriptor, descriptor.cooldownTicks());
    }

    @Override
    protected void analyze(PacketEvent event, PlayerData data, long tick) {
        if (event.getPacketType() != PacketType.Play.Client.BLOCK_PLACE) {
            return;
        }

        PrimitiveRingBuffer place = data.placementIntervalBuffer();
        PrimitiveRingBuffer rot = data.rotationDeltaBuffer();
        if (place.count() < 16 || rot.count() < 16) {
            return;
        }

        boolean intervalUniform = place.coefficientOfVariation() < 0.10D;
        boolean mechanicalAim = rot.coefficientOfVariation() < 0.14D && rot.entropy8() < 1.40D;

        if (intervalUniform && mechanicalAim) {
            registerAnomaly();
            addSuspicion(11.0D);
        }
    }
}
