package judexis.core.evidence;

public final class EvidenceRecord {

    private final long timestamp;
    private final String module;
    private final double suspicionDelta;
    private final int phaseAtTime;
    private final double reliability;
    private final long tick;

    public EvidenceRecord(long timestamp, String module, double suspicionDelta, int phaseAtTime, double reliability, long tick) {
        this.timestamp = timestamp;
        this.module = module;
        this.suspicionDelta = suspicionDelta;
        this.phaseAtTime = phaseAtTime;
        this.reliability = reliability;
        this.tick = tick;
    }

    public long timestamp() {
        return timestamp;
    }

    public String module() {
        return module;
    }

    public double suspicionDelta() {
        return suspicionDelta;
    }

    public int phase() {
        return phaseAtTime;
    }

    public double reliability() {
        return reliability;
    }

    public long tick() {
        return tick;
    }
}
