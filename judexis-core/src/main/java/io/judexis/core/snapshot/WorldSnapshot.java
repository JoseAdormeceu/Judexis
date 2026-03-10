package io.judexis.core.snapshot;

/**
 * Snapshot for state tied to world and collision transitions.
 */
public final class WorldSnapshot implements Snapshot {
    private final long tick;
    private final long capturedAtNanos;
    private final String worldId;
    private final int blockX;
    private final int blockY;
    private final int blockZ;
    private final WorldMedium medium;
    private final boolean collidedHorizontally;

    public WorldSnapshot(long tick, long capturedAtNanos, String worldId, int blockX, int blockY,
                         int blockZ, WorldMedium medium, boolean collidedHorizontally) {
        if (worldId == null || worldId.trim().isEmpty()) {
            throw new IllegalArgumentException("worldId is required");
        }
        if (medium == null) {
            throw new IllegalArgumentException("medium is required");
        }
        this.tick = tick;
        this.capturedAtNanos = capturedAtNanos;
        this.worldId = worldId;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.medium = medium;
        this.collidedHorizontally = collidedHorizontally;
    }

    public long getTick() {
        return tick;
    }

    public long getCapturedAtNanos() {
        return capturedAtNanos;
    }

    public SnapshotType getType() {
        return SnapshotType.WORLD;
    }

    public String getWorldId() {
        return worldId;
    }

    public int getBlockX() {
        return blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public WorldMedium getMedium() {
        return medium;
    }

    public boolean isCollidedHorizontally() {
        return collidedHorizontally;
    }
}
