package judexis.core.context;

import judexis.core.player.PlayerSnapshot;

public interface ContextReliabilityEngine {
    double reliability(PlayerSnapshot snapshot, long now);
}
