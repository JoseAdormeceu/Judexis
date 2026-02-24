package judexis.core.behavior;

import com.comphenix.protocol.events.PacketEvent;
import judexis.core.player.runtime.PlayerData;

public abstract class BehavioralModule {

    protected double suspicion;
    protected final double threshold;
    protected int anomalyCounter;
    protected final int requiredAnomalies;
    protected long lastTickProcessed;
    protected final long cooldownTicks;
    private final String moduleId;
    private double currentReliability;
    private double lastSuspicionDelta;

    protected BehavioralModule(ModuleDescriptor descriptor, long cooldownTicks) {
        this.moduleId = descriptor.moduleId();
        this.threshold = descriptor.threshold();
        this.requiredAnomalies = descriptor.requiredAnomalies();
        this.cooldownTicks = cooldownTicks;
        this.lastTickProcessed = -1L;
        this.currentReliability = 1.0D;
        this.lastSuspicionDelta = 0.0D;
    }

    public final String moduleId() {
        return moduleId;
    }

    public final double suspicion() {
        return suspicion;
    }

    public final int anomalyCounter() {
        return anomalyCounter;
    }

    public final void handle(PacketEvent event, PlayerData data, long tick, double reliability) {
        this.lastSuspicionDelta = 0.0D;
        if (lastTickProcessed >= 0L && tick - lastTickProcessed < cooldownTicks) {
            return;
        }
        this.currentReliability = reliability;
        this.lastTickProcessed = tick;
        analyze(event, data, tick);
    }

    protected abstract void analyze(PacketEvent event, PlayerData data, long tick);

    protected final void addSuspicion(double amount) {
        double scaled = amount * currentReliability;
        this.suspicion += scaled;
        this.lastSuspicionDelta += scaled;
    }

    public void decayPerTick(double decayAmount) {
        if (decayAmount <= 0.0D) {
            return;
        }
        this.suspicion -= decayAmount;
        if (this.suspicion < 0.0D) {
            this.suspicion = 0.0D;
        }
    }

    public final boolean thresholdReached() {
        return this.suspicion >= this.threshold && this.anomalyCounter >= this.requiredAnomalies;
    }

    public final void registerAnomaly() {
        if (anomalyCounter < Integer.MAX_VALUE) {
            anomalyCounter++;
        }
    }

    public final double consumeLastSuspicionDelta() {
        double value = this.lastSuspicionDelta;
        this.lastSuspicionDelta = 0.0D;
        return value;
    }

    public final void onThresholdReached() {
        this.anomalyCounter = 0;
    }
}
