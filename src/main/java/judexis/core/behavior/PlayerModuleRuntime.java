package judexis.core.behavior;

import com.comphenix.protocol.events.PacketEvent;
import judexis.core.player.runtime.PlayerData;
import judexis.core.suspicion.SuspicionEngine;

public final class PlayerModuleRuntime {

    private final BehavioralModule[] modules;

    public PlayerModuleRuntime() {
        this.modules = ModuleFactory.createAll();
    }

    public void handle(PacketEvent event, PlayerData data, long tick, double reliability, SuspicionEngine suspicionEngine) {
        for (int i = 0; i < modules.length; i++) {
            BehavioralModule module = modules[i];
            module.handle(event, data, tick, reliability);
            double delta = module.consumeLastSuspicionDelta();
            suspicionEngine.process(data, module, delta, reliability, tick);
        }
    }

    public void decay(double amount) {
        for (int i = 0; i < modules.length; i++) {
            modules[i].decayPerTick(amount);
        }
    }
}
