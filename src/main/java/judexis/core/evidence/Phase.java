package judexis.core.evidence;

public enum Phase {
    MONITORING(1),
    SUSPICION_ESCALATED(2),
    CRITICAL(3),
    ENFORCEMENT_AUTHORIZED(4);

    private final int level;

    Phase(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }
}
