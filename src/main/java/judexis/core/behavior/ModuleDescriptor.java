package judexis.core.behavior;

public final class ModuleDescriptor {

    private final String moduleId;
    private final ModuleCategory category;
    private final double threshold;
    private final int requiredAnomalies;
    private final long cooldownTicks;

    public ModuleDescriptor(String moduleId, ModuleCategory category, double threshold, int requiredAnomalies, long cooldownTicks) {
        this.moduleId = moduleId;
        this.category = category;
        this.threshold = threshold;
        this.requiredAnomalies = requiredAnomalies;
        this.cooldownTicks = cooldownTicks;
    }

    public String moduleId() {
        return moduleId;
    }

    public ModuleCategory category() {
        return category;
    }

    public double threshold() {
        return threshold;
    }

    public int requiredAnomalies() {
        return requiredAnomalies;
    }

    public long cooldownTicks() {
        return cooldownTicks;
    }
}
