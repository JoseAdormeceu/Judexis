package judexis.core.behavior.modules.combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import judexis.core.behavior.BehavioralModule;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.player.runtime.PlayerData;
import judexis.core.player.runtime.PrimitiveRingBuffer;

public final class KillAuraEntropyModule extends BehavioralModule {

    public KillAuraEntropyModule(ModuleDescriptor descriptor) {
        super(descriptor, descriptor.cooldownTicks());
    }

    @Override
    protected void analyze(PacketEvent event, PlayerData data, long tick) {
        PacketType type = event.getPacketType();
        if (type != PacketType.Play.Client.USE_ENTITY && type != PacketType.Play.Client.ARM_ANIMATION) {
            return;
        }

        PrimitiveRingBuffer rotation = data.rotationDeltaBuffer();
        PrimitiveRingBuffer attacks = data.attackIntervalBuffer();
        PrimitiveRingBuffer switches = data.targetSwitchIntervalBuffer();
        if (rotation.count() < 20 || attacks.count() < 20 || switches.count() < 8) {
            return;
        }

        double entropy = rotation.entropy8();
        double attackCv = attacks.coefficientOfVariation();
        double switchCv = switches.coefficientOfVariation();
        double p95 = rotation.percentile95();
        double micro = Math.abs(rotation.valueAt(0) - rotation.valueAt(1));

        boolean smooth = entropy < 1.35D && attackCv < 0.16D;
        boolean lowMicroCorrection = micro < 0.015D && p95 > 2.9D;
        boolean mechanicalSwitch = switchCv < 0.20D;

        if (smooth && lowMicroCorrection && mechanicalSwitch) {
            registerAnomaly();
            addSuspicion(14.0D);
        }
    }
}
