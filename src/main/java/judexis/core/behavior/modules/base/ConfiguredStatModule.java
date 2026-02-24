package judexis.core.behavior.modules.base;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import judexis.core.behavior.BehavioralModule;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.player.runtime.PlayerData;
import judexis.core.player.runtime.PrimitiveRingBuffer;

public class ConfiguredStatModule extends BehavioralModule {

    private final PacketType gateA;
    private final PacketType gateB;
    private final BufferChannel primary;
    private final BufferChannel secondary;
    private final int minSamples;
    private final double cvLimit;
    private final double entropyLimit;
    private final double p95Limit;
    private final double suspicionWeight;

    public ConfiguredStatModule(ModuleDescriptor descriptor,
                                PacketType gateA,
                                PacketType gateB,
                                BufferChannel primary,
                                BufferChannel secondary,
                                int minSamples,
                                double cvLimit,
                                double entropyLimit,
                                double p95Limit,
                                double suspicionWeight) {
        super(descriptor, descriptor.cooldownTicks());
        this.gateA = gateA;
        this.gateB = gateB;
        this.primary = primary;
        this.secondary = secondary;
        this.minSamples = minSamples;
        this.cvLimit = cvLimit;
        this.entropyLimit = entropyLimit;
        this.p95Limit = p95Limit;
        this.suspicionWeight = suspicionWeight;
    }

    @Override
    protected void analyze(PacketEvent event, PlayerData data, long tick) {
        PacketType type = event.getPacketType();
        if (type != gateA && type != gateB) {
            return;
        }

        PrimitiveRingBuffer first = select(data, primary);
        PrimitiveRingBuffer second = select(data, secondary);
        if (first.count() < minSamples || second.count() < minSamples) {
            return;
        }

        double cvFirst = first.coefficientOfVariation();
        double cvSecond = second.coefficientOfVariation();
        double entropy = first.entropy8();
        double p95 = first.percentile95();
        double recentDiff = Math.abs(first.valueAt(0) - first.valueAt(1));

        boolean sustainedPattern = cvFirst <= cvLimit && cvSecond <= cvLimit;
        boolean mechanicalShape = entropy <= entropyLimit;
        boolean abnormalTail = p95 >= p95Limit || recentDiff <= 0.02D;

        if (sustainedPattern && mechanicalShape && abnormalTail) {
            registerAnomaly();
            addSuspicion(suspicionWeight);
        }
    }

    protected final PrimitiveRingBuffer select(PlayerData data, BufferChannel channel) {
        switch (channel) {
            case HORIZONTAL:
                return data.horizontalSpeedBuffer();
            case VERTICAL:
                return data.verticalSpeedBuffer();
            case ROTATION:
                return data.rotationDeltaBuffer();
            case YAW:
                return data.yawDeltaBuffer();
            case PITCH:
                return data.pitchDeltaBuffer();
            case ATTACK_INTERVAL:
                return data.attackIntervalBuffer();
            case PLACE_INTERVAL:
                return data.placementIntervalBuffer();
            case BREAK_INTERVAL:
                return data.breakIntervalBuffer();
            case TRANSACTION_INTERVAL:
                return data.transactionIntervalBuffer();
            case PACKET_INTERVAL:
                return data.packetIntervalBuffer();
            case REACH_DISTANCE:
                return data.reachDistanceBuffer();
            case VELOCITY_REDUCTION:
                return data.velocityReductionBuffer();
            case STRAFE_ERROR:
                return data.strafeAngleErrorBuffer();
            case DISPLACEMENT_BURST:
                return data.displacementBurstBuffer();
            case TARGET_SWITCH_INTERVAL:
                return data.targetSwitchIntervalBuffer();
            case FOV_MISMATCH:
                return data.fovMismatchBuffer();
            default:
                return data.rotationDeltaBuffer();
        }
    }
}
