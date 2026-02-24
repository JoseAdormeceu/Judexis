package judexis.core.player.runtime;

import judexis.core.config.ConfigurationService;
import judexis.core.player.PlayerDataSnapshotLayer;
import judexis.core.player.PlayerSnapshot;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Collection;

public final class PlayerDataRepository implements PlayerDataSnapshotLayer {

    private final ConcurrentHashMap<UUID, PlayerData> players;
    private final int ringSize;

    public PlayerDataRepository(ConfigurationService configurationService) {
        this.players = new ConcurrentHashMap<UUID, PlayerData>();
        this.ringSize = configurationService.ringBufferSize();
    }

    @Override
    public PlayerSnapshot getSnapshot(UUID playerId) {
        return players.get(playerId);
    }

    public PlayerData getPlayerData(UUID playerId) {
        return players.get(playerId);
    }

    @Override
    public void ensurePlayer(UUID playerId, String playerName) {
        players.computeIfAbsent(playerId, ignored -> new PlayerData(playerId, playerName, ringSize));
    }

    @Override
    public void removePlayer(UUID playerId) {
        players.remove(playerId);
    }

    @Override
    public Collection<PlayerSnapshot> allSnapshots() {
        return new ArrayList<PlayerSnapshot>(players.values());
    }
}
