package judexis.core.evidence;

import judexis.core.behavior.BehavioralModule;
import judexis.core.enforcement.EnforcementAction;
import judexis.core.evidence.EvidenceEngine;
import judexis.core.evidence.Phase;
import judexis.core.player.PlayerSnapshot;

import java.util.Deque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public final class EvidenceManager implements EvidenceEngine {

    private final Map<UUID, Deque<EvidenceRecord>> evidenceByPlayer;
    private final Map<UUID, Deque<TimelineRecord>> timelineByPlayer;
    private final Map<UUID, PhaseState> phaseStateByPlayer;

    public EvidenceManager() {
        this.evidenceByPlayer = new ConcurrentHashMap<UUID, Deque<EvidenceRecord>>();
        this.timelineByPlayer = new ConcurrentHashMap<UUID, Deque<TimelineRecord>>();
        this.phaseStateByPlayer = new ConcurrentHashMap<UUID, PhaseState>();
    }

    @Override
    public void recordEvidence(PlayerSnapshot snapshot, BehavioralModule module, double suspicionDelta, double reliability, long tick) {
        UUID key = snapshot.getPlayerId();
        PhaseState state = phaseStateByPlayer.computeIfAbsent(key, ignored -> new PhaseState());
        state.cumulativeSuspicion += suspicionDelta;
        Phase next = phaseFor(state.cumulativeSuspicion);

        Deque<EvidenceRecord> evidence = evidenceByPlayer.computeIfAbsent(key, ignored -> new ConcurrentLinkedDeque<EvidenceRecord>());
        evidence.addLast(new EvidenceRecord(System.currentTimeMillis(), module.moduleId(), suspicionDelta, next.level(), reliability, tick));
        if (evidence.size() > 256) {
            evidence.removeFirst();
        }

        if (next != state.phase) {
            state.phase = next;
            Deque<TimelineRecord> timeline = timelineByPlayer.computeIfAbsent(key, ignored -> new ConcurrentLinkedDeque<TimelineRecord>());
            timeline.addLast(new TimelineRecord(System.currentTimeMillis(), next, "PHASE_ESCALATION"));
            if (timeline.size() > 128) {
                timeline.removeFirst();
            }
        }
    }

    @Override
    public Phase currentPhase(PlayerSnapshot snapshot) {
        PhaseState state = phaseStateByPlayer.get(snapshot.getPlayerId());
        if (state == null) {
            return Phase.MONITORING;
        }
        return state.phase;
    }

    @Override
    public void recordEnforcement(PlayerSnapshot snapshot, EnforcementAction action, long tick) {
        UUID key = snapshot.getPlayerId();
        Deque<TimelineRecord> timeline = timelineByPlayer.computeIfAbsent(key, ignored -> new ConcurrentLinkedDeque<TimelineRecord>());
        timeline.addLast(new TimelineRecord(System.currentTimeMillis(), Phase.ENFORCEMENT_AUTHORIZED, action.name() + "@" + tick));
        if (timeline.size() > 128) {
            timeline.removeFirst();
        }
        PhaseState state = phaseStateByPlayer.computeIfAbsent(key, ignored -> new PhaseState());
        state.phase = Phase.ENFORCEMENT_AUTHORIZED;
    }

    private Phase phaseFor(double cumulativeSuspicion) {
        if (cumulativeSuspicion >= 220.0D) {
            return Phase.ENFORCEMENT_AUTHORIZED;
        }
        if (cumulativeSuspicion >= 155.0D) {
            return Phase.CRITICAL;
        }
        if (cumulativeSuspicion >= 80.0D) {
            return Phase.SUSPICION_ESCALATED;
        }
        return Phase.MONITORING;
    }

    private static final class PhaseState {
        private Phase phase = Phase.MONITORING;
        private double cumulativeSuspicion;
    }
}
