package judexis.core.player;

import java.util.UUID;

import java.util.Collection;

public interface PlayerDataSnapshotLayer {
    PlayerSnapshot getSnapshot(UUID playerId);
    void ensurePlayer(UUID playerId, String playerName);
    void removePlayer(UUID playerId);
    Collection<PlayerSnapshot> allSnapshots();
}
