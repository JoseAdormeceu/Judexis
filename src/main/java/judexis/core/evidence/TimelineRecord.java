package judexis.core.evidence;

import judexis.core.evidence.Phase;

public final class TimelineRecord {

    private final long timestamp;
    private final Phase phase;
    private final String event;

    public TimelineRecord(long timestamp, Phase phase, String event) {
        this.timestamp = timestamp;
        this.phase = phase;
        this.event = event;
    }

    public long timestamp() {
        return timestamp;
    }

    public Phase phase() {
        return phase;
    }

    public String reason() {
        return event;
    }
}
