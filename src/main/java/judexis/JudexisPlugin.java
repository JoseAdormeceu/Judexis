package judexis;

import judexis.core.JudexisCore;
import org.bukkit.plugin.java.JavaPlugin;

public final class JudexisPlugin extends JavaPlugin {

    private JudexisCore core;

    @Override
    public void onEnable() {
        printBanner();
        saveDefaultConfig();
        this.core = new JudexisCore(this);
        this.core.start();
    }

    @Override
    public void onDisable() {
        if (this.core != null) {
            this.core.stop();
        }
    }

    private void printBanner() {
        getLogger().info("=====================================");
        getLogger().info("Judexis AntiCheat");
        getLogger().info("Commercial Edition");
        getLogger().info("Engine: Judexis Core");
        getLogger().info("Version: 5.0-BetaTest");
        getLogger().info("=====================================");
    }
}