package judexis.core.suspicion;

import judexis.core.behavior.BehavioralModule;
import judexis.core.evidence.EvidenceEngine;
import judexis.core.player.PlayerSnapshot;

public final class DefaultSuspicionEngine implements SuspicionEngine {

    private final EvidenceEngine evidenceEngine;

    public DefaultSuspicionEngine(EvidenceEngine evidenceEngine) {
        this.evidenceEngine = evidenceEngine;
    }

    @Override
    public void process(PlayerSnapshot snapshot, BehavioralModule module, double suspicionDelta, double reliability, long tick) {
        if (suspicionDelta <= 0.0D) {
            return;
        }

        evidenceEngine.recordEvidence(snapshot, module, suspicionDelta, reliability, tick);
        if (module.thresholdReached()) {
            module.onThresholdReached();
        }
    }
}