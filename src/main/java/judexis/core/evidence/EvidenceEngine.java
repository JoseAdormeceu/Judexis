package judexis.core.evidence;

import judexis.core.behavior.BehavioralModule;
import judexis.core.enforcement.EnforcementAction;
import judexis.core.player.PlayerSnapshot;

public interface EvidenceEngine {
    void recordEvidence(PlayerSnapshot snapshot, BehavioralModule module, double suspicionDelta, double reliability, long tick);
    Phase currentPhase(PlayerSnapshot snapshot);
    void recordEnforcement(PlayerSnapshot snapshot, EnforcementAction action, long tick);
}
