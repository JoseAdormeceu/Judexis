package judexis.core.profile;

public interface ProfileEngine {
    DetectionProfile activeProfile();
    void switchProfile(DetectionProfile profile);
    boolean panicMode();
    void setPanicMode(boolean enabled);
    boolean maintenanceMode();
    void setMaintenanceMode(boolean enabled);
}
