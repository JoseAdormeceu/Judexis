package judexis.core.profile;

import judexis.core.audit.AuditLayer;
import judexis.core.config.ConfigurationService;
import judexis.core.profile.DetectionProfile;
import judexis.core.profile.ProfileEngine;

import java.util.UUID;

public final class ProfileManager implements ProfileEngine {

    private final AuditLayer auditLayer;
    private volatile DetectionProfile active;
    private volatile boolean panicMode;
    private volatile boolean maintenanceMode;

    public ProfileManager(ConfigurationService config, AuditLayer auditLayer) {
        this.auditLayer = auditLayer;
        this.active = config.defaultProfile();
        this.panicMode = false;
        this.maintenanceMode = false;
    }

    @Override
    public DetectionProfile activeProfile() {
        return active;
    }

    @Override
    public void switchProfile(DetectionProfile profile) {
        this.active = profile;
        this.auditLayer.log("PROFILE_SWITCH", new UUID(0L, 0L), profile.name(), true);
    }

    @Override
    public boolean panicMode() {
        return panicMode;
    }

    @Override
    public void setPanicMode(boolean enabled) {
        this.panicMode = enabled;
    }

    @Override
    public boolean maintenanceMode() {
        return maintenanceMode;
    }

    @Override
    public void setMaintenanceMode(boolean enabled) {
        this.maintenanceMode = enabled;
    }
}
