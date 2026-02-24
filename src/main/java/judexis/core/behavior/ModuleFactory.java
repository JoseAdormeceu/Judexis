package judexis.core.behavior;

import judexis.core.behavior.BehavioralModule;
import judexis.core.behavior.ModuleCatalog;
import judexis.core.behavior.ModuleDescriptor;
import judexis.core.behavior.modules.combat.AutoClickerIntervalModule;
import judexis.core.behavior.modules.combat.CombatAimDistributionModule;
import judexis.core.behavior.modules.combat.KillAuraEntropyModule;
import judexis.core.behavior.modules.combat.KillAuraGcdSnapModule;
import judexis.core.behavior.modules.combat.KillAuraMicroCorrectionModule;
import judexis.core.behavior.modules.combat.KillAuraTargetSwitchModule;
import judexis.core.behavior.modules.combat.NoKnockbackSimulationModule;
import judexis.core.behavior.modules.combat.ReachRaytraceModule;
import judexis.core.behavior.modules.combat.SilentAimFovMismatchModule;
import judexis.core.behavior.modules.combat.TriggerBotReactionModule;
import judexis.core.behavior.modules.combat.VelocityReductionClusterModule;
import judexis.core.behavior.modules.combat.WTapMacroPatternModule;
import judexis.core.behavior.modules.movement.AirAccelerationModule;
import judexis.core.behavior.modules.movement.FlyGravityModule;
import judexis.core.behavior.modules.movement.GlideLowGravityModule;
import judexis.core.behavior.modules.movement.JesusLiquidWalkModule;
import judexis.core.behavior.modules.movement.MovementTimerDesyncModule;
import judexis.core.behavior.modules.movement.NoFallGroundSpoofModule;
import judexis.core.behavior.modules.movement.PotionSpeedAnomalyModule;
import judexis.core.behavior.modules.movement.SpeedFrictionModule;
import judexis.core.behavior.modules.movement.SprintModifierDriftModule;
import judexis.core.behavior.modules.movement.StepPatternModule;
import judexis.core.behavior.modules.movement.StrafeAngleCvModule;
import judexis.core.behavior.modules.packet.BlinkBurstModule;
import judexis.core.behavior.modules.packet.PacketOrderSequenceModule;
import judexis.core.behavior.modules.packet.PingSpoofRttModule;
import judexis.core.behavior.modules.packet.RotationPacketGcdModule;
import judexis.core.behavior.modules.packet.RotationPacketRepeatDeltaModule;
import judexis.core.behavior.modules.packet.TimerPpsSustainModule;
import judexis.core.behavior.modules.world.BlockReachWorldModule;
import judexis.core.behavior.modules.world.FastBreakHardnessModule;
import judexis.core.behavior.modules.world.GhostHandDistanceModule;
import judexis.core.behavior.modules.world.InvalidInteractVectorModule;
import judexis.core.behavior.modules.world.NukerBurstModule;
import judexis.core.behavior.modules.world.PhaseAabbCollisionModule;
import judexis.core.behavior.modules.world.ScaffoldAngleConsistencyModule;
import judexis.core.behavior.modules.world.ScaffoldBackwardPlacementModule;
import judexis.core.behavior.modules.world.ScaffoldIntervalUniformityModule;
import judexis.core.behavior.modules.world.WorldOrderAnomalyModule;

public final class ModuleFactory {

    private ModuleFactory() {
    }

    public static BehavioralModule[] createAll() {
        BehavioralModule[] modules = new BehavioralModule[ModuleCatalog.modules().size()];
        for (int i = 0; i < ModuleCatalog.modules().size(); i++) {
            modules[i] = create(ModuleCatalog.modules().get(i));
        }
        return modules;
    }

    private static BehavioralModule create(ModuleDescriptor descriptor) {
        String id = descriptor.moduleId();

        if ("KillAuraEntropy".equals(id)) return new KillAuraEntropyModule(descriptor);
        if ("KillAuraGcdSnap".equals(id)) return new KillAuraGcdSnapModule(descriptor);
        if ("KillAuraMicroCorrection".equals(id)) return new KillAuraMicroCorrectionModule(descriptor);
        if ("KillAuraTargetSwitch".equals(id)) return new KillAuraTargetSwitchModule(descriptor);
        if ("ReachRaytrace".equals(id)) return new ReachRaytraceModule(descriptor);
        if ("AutoClickerInterval".equals(id)) return new AutoClickerIntervalModule(descriptor);
        if ("SilentAimFovMismatch".equals(id)) return new SilentAimFovMismatchModule(descriptor);
        if ("TriggerBotReaction".equals(id)) return new TriggerBotReactionModule(descriptor);
        if ("NoKnockbackSimulation".equals(id)) return new NoKnockbackSimulationModule(descriptor);
        if ("VelocityReductionCluster".equals(id)) return new VelocityReductionClusterModule(descriptor);
        if ("WTapMacroPattern".equals(id)) return new WTapMacroPatternModule(descriptor);
        if ("CombatAimDistribution".equals(id)) return new CombatAimDistributionModule(descriptor);

        if ("SpeedFriction".equals(id)) return new SpeedFrictionModule(descriptor);
        if ("FlyGravity".equals(id)) return new FlyGravityModule(descriptor);
        if ("GlideLowGravity".equals(id)) return new GlideLowGravityModule(descriptor);
        if ("NoFallGroundSpoof".equals(id)) return new NoFallGroundSpoofModule(descriptor);
        if ("JesusLiquidWalk".equals(id)) return new JesusLiquidWalkModule(descriptor);
        if ("StrafeAngleCv".equals(id)) return new StrafeAngleCvModule(descriptor);
        if ("SprintModifierDrift".equals(id)) return new SprintModifierDriftModule(descriptor);
        if ("PotionSpeedAnomaly".equals(id)) return new PotionSpeedAnomalyModule(descriptor);
        if ("AirAcceleration".equals(id)) return new AirAccelerationModule(descriptor);
        if ("StepPattern".equals(id)) return new StepPatternModule(descriptor);
        if ("MovementTimerDesync".equals(id)) return new MovementTimerDesyncModule(descriptor);

        if ("ScaffoldAngleConsistency".equals(id)) return new ScaffoldAngleConsistencyModule(descriptor);
        if ("ScaffoldIntervalUniformity".equals(id)) return new ScaffoldIntervalUniformityModule(descriptor);
        if ("ScaffoldBackwardPlacement".equals(id)) return new ScaffoldBackwardPlacementModule(descriptor);
        if ("FastBreakHardness".equals(id)) return new FastBreakHardnessModule(descriptor);
        if ("NukerBurst".equals(id)) return new NukerBurstModule(descriptor);
        if ("GhostHandDistance".equals(id)) return new GhostHandDistanceModule(descriptor);
        if ("PhaseAabbCollision".equals(id)) return new PhaseAabbCollisionModule(descriptor);
        if ("BlockReachWorld".equals(id)) return new BlockReachWorldModule(descriptor);
        if ("InvalidInteractVector".equals(id)) return new InvalidInteractVectorModule(descriptor);
        if ("WorldOrderAnomaly".equals(id)) return new WorldOrderAnomalyModule(descriptor);

        if ("TimerPpsSustain".equals(id)) return new TimerPpsSustainModule(descriptor);
        if ("BlinkBurst".equals(id)) return new BlinkBurstModule(descriptor);
        if ("PingSpoofRtt".equals(id)) return new PingSpoofRttModule(descriptor);
        if ("RotationPacketGcd".equals(id)) return new RotationPacketGcdModule(descriptor);
        if ("RotationPacketRepeatDelta".equals(id)) return new RotationPacketRepeatDeltaModule(descriptor);
        if ("PacketOrderSequence".equals(id)) return new PacketOrderSequenceModule(descriptor);

        throw new IllegalArgumentException("Unknown module descriptor: " + id);
    }
}
