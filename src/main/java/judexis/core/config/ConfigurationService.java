package judexis.core.config;

import judexis.core.profile.DetectionProfile;

public interface ConfigurationService {
    void reload();
    int ringBufferSize();
    boolean enforcementEnabled();
    boolean debugEnabled();
    double baseDecayPerTick();
    DetectionProfile defaultProfile();
    int teleportGraceMs();
    int velocityGraceMs();
    int joinGraceMs();
    int pingHighThreshold();
    double lowTpsThreshold();
    int packetIrregularityGrace();
}
