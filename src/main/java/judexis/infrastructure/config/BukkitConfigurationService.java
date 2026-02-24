package judexis.infrastructure.config;

import judexis.JudexisPlugin;
import judexis.core.config.ConfigurationService;
import judexis.core.profile.DetectionProfile;
import org.bukkit.configuration.file.FileConfiguration;

public final class BukkitConfigurationService implements ConfigurationService {

    private final JudexisPlugin plugin;
    private volatile FileConfiguration configuration;

    public BukkitConfigurationService(JudexisPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfig();
    }

    @Override
    public void reload() {
        plugin.reloadConfig();
        this.configuration = plugin.getConfig();
    }

    @Override
    public int ringBufferSize() {
        return configuration.getInt("performance.ring-buffer-size", 64);
    }

    @Override
    public boolean enforcementEnabled() {
        return configuration.getBoolean("enforcement.enabled", true);
    }

    @Override
    public boolean debugEnabled() {
        return configuration.getBoolean("diagnostics.debug", false);
    }

    @Override
    public double baseDecayPerTick() {
        return configuration.getDouble("performance.base-decay-per-tick", 0.65D);
    }

    @Override
    public DetectionProfile defaultProfile() {
        String value = configuration.getString("profiles.default", "BALANCED");
        try {
            return DetectionProfile.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return DetectionProfile.BALANCED;
        }
    }

    @Override
    public int teleportGraceMs() {
        return configuration.getInt("context.teleport-grace-ms", 1500);
    }

    @Override
    public int velocityGraceMs() {
        return configuration.getInt("context.velocity-grace-ms", 1200);
    }

    @Override
    public int joinGraceMs() {
        return configuration.getInt("context.join-grace-ms", 7000);
    }

    @Override
    public int pingHighThreshold() {
        return configuration.getInt("context.ping-high-threshold", 180);
    }

    @Override
    public double lowTpsThreshold() {
        return configuration.getDouble("context.low-tps-threshold", 19.5D);
    }

    @Override
    public int packetIrregularityGrace() {
        return configuration.getInt("performance.packet-irregularity-grace", 12);
    }
}
