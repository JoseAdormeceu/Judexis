package judexis.core.profile;

public enum DetectionProfile {
    SAFE(0.75D, false, false, 0.65D),
    BALANCED(1.0D, true, true, 0.8D),
    STRICT(1.2D, true, true, 0.95D);

    private final double suspicionMultiplier;
    private final boolean enforcementEnabled;
    private final boolean freezeEnabled;
    private final double alertSensitivity;

    DetectionProfile(double suspicionMultiplier, boolean enforcementEnabled, boolean freezeEnabled, double alertSensitivity) {
        this.suspicionMultiplier = suspicionMultiplier;
        this.enforcementEnabled = enforcementEnabled;
        this.freezeEnabled = freezeEnabled;
        this.alertSensitivity = alertSensitivity;
    }

    public double suspicionMultiplier() {
        return suspicionMultiplier;
    }

    public boolean enforcementEnabled() {
        return enforcementEnabled;
    }

    public boolean freezeEnabled() {
        return freezeEnabled;
    }

    public double alertSensitivity() {
        return alertSensitivity;
    }
}
