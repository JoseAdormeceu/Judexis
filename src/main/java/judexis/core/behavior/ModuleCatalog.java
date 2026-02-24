package judexis.core.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleCatalog {

    private static final List<ModuleDescriptor> MODULES;

    static {
        List<ModuleDescriptor> descriptors = new ArrayList<ModuleDescriptor>(39);
        descriptors.add(new ModuleDescriptor("KillAuraEntropy", ModuleCategory.COMBAT, 95.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("KillAuraGcdSnap", ModuleCategory.COMBAT, 92.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("KillAuraMicroCorrection", ModuleCategory.COMBAT, 90.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("KillAuraTargetSwitch", ModuleCategory.COMBAT, 96.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("ReachRaytrace", ModuleCategory.COMBAT, 105.0D, 6, 1));
        descriptors.add(new ModuleDescriptor("AutoClickerInterval", ModuleCategory.COMBAT, 92.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("SilentAimFovMismatch", ModuleCategory.COMBAT, 98.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("TriggerBotReaction", ModuleCategory.COMBAT, 94.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("NoKnockbackSimulation", ModuleCategory.COMBAT, 104.0D, 6, 1));
        descriptors.add(new ModuleDescriptor("VelocityReductionCluster", ModuleCategory.COMBAT, 100.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("WTapMacroPattern", ModuleCategory.COMBAT, 92.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("CombatAimDistribution", ModuleCategory.COMBAT, 90.0D, 8, 1));

        descriptors.add(new ModuleDescriptor("SpeedFriction", ModuleCategory.MOVEMENT, 104.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("FlyGravity", ModuleCategory.MOVEMENT, 100.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("GlideLowGravity", ModuleCategory.MOVEMENT, 96.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("NoFallGroundSpoof", ModuleCategory.MOVEMENT, 98.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("JesusLiquidWalk", ModuleCategory.MOVEMENT, 98.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("StrafeAngleCv", ModuleCategory.MOVEMENT, 96.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("SprintModifierDrift", ModuleCategory.MOVEMENT, 94.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("PotionSpeedAnomaly", ModuleCategory.MOVEMENT, 94.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("AirAcceleration", ModuleCategory.MOVEMENT, 96.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("StepPattern", ModuleCategory.MOVEMENT, 94.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("MovementTimerDesync", ModuleCategory.MOVEMENT, 102.0D, 6, 1));

        descriptors.add(new ModuleDescriptor("ScaffoldAngleConsistency", ModuleCategory.WORLD, 94.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("ScaffoldIntervalUniformity", ModuleCategory.WORLD, 95.0D, 8, 1));
        descriptors.add(new ModuleDescriptor("ScaffoldBackwardPlacement", ModuleCategory.WORLD, 96.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("FastBreakHardness", ModuleCategory.WORLD, 100.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("NukerBurst", ModuleCategory.WORLD, 102.0D, 6, 1));
        descriptors.add(new ModuleDescriptor("GhostHandDistance", ModuleCategory.WORLD, 104.0D, 6, 1));
        descriptors.add(new ModuleDescriptor("PhaseAabbCollision", ModuleCategory.WORLD, 106.0D, 6, 1));
        descriptors.add(new ModuleDescriptor("BlockReachWorld", ModuleCategory.WORLD, 102.0D, 6, 1));
        descriptors.add(new ModuleDescriptor("InvalidInteractVector", ModuleCategory.WORLD, 98.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("WorldOrderAnomaly", ModuleCategory.WORLD, 96.0D, 8, 1));

        descriptors.add(new ModuleDescriptor("TimerPpsSustain", ModuleCategory.PACKET, 102.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("BlinkBurst", ModuleCategory.PACKET, 103.0D, 6, 1));
        descriptors.add(new ModuleDescriptor("PingSpoofRtt", ModuleCategory.PACKET, 100.0D, 7, 1));
        descriptors.add(new ModuleDescriptor("RotationPacketGcd", ModuleCategory.PACKET, 92.0D, 9, 1));
        descriptors.add(new ModuleDescriptor("RotationPacketRepeatDelta", ModuleCategory.PACKET, 92.0D, 9, 1));
        descriptors.add(new ModuleDescriptor("PacketOrderSequence", ModuleCategory.PACKET, 94.0D, 9, 1));

        MODULES = Collections.unmodifiableList(descriptors);
    }

    private ModuleCatalog() {
    }

    public static List<ModuleDescriptor> modules() {
        return MODULES;
    }
}
