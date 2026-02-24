package judexis.core.suspicion;

import judexis.core.behavior.BehavioralModule;
import judexis.core.player.PlayerSnapshot;

public interface SuspicionEngine {
    void process(PlayerSnapshot snapshot, BehavioralModule module, double suspicionDelta, double reliability, long tick);
}