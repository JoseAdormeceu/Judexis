package judexis.core.enforcement;

import judexis.core.evidence.Phase;
import judexis.core.player.PlayerSnapshot;
import judexis.core.profile.DetectionProfile;

import java.util.UUID;

public interface EnforcementEngine {
    void evaluate(PlayerSnapshot snapshot, Phase phase, DetectionProfile profile, long tick);
    void freeze(UUID playerId, String actorName);
    void unfreeze(UUID playerId, String actorName);
    void setEnforcementEnabled(boolean enabled);
    boolean isEnforcementEnabled();
}
