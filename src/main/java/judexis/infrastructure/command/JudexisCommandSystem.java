package judexis.infrastructure.command;

import judexis.JudexisPlugin;
import judexis.core.audit.AuditLayer;
import judexis.core.command.CommandSystem;
import judexis.core.config.ConfigurationService;
import judexis.core.enforcement.EnforcementEngine;
import judexis.core.gui.GuiControlSuite;
import judexis.core.profile.DetectionProfile;
import judexis.core.profile.ProfileEngine;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class JudexisCommandSystem implements CommandSystem, CommandExecutor, TabCompleter {

    private final JudexisPlugin plugin;
    private final ProfileEngine profileEngine;
    private final GuiControlSuite gui;
    private final ConfigurationService configurationService;
    private final EnforcementEngine enforcementEngine;
    private final AuditLayer auditLayer;
    private final SubCommandRegistry registry;

    public JudexisCommandSystem(JudexisPlugin plugin,
                                ProfileEngine profileEngine,
                                GuiControlSuite gui,
                                ConfigurationService configurationService,
                                EnforcementEngine enforcementEngine,
                                AuditLayer auditLayer) {
        this.plugin = plugin;
        this.profileEngine = profileEngine;
        this.gui = gui;
        this.configurationService = configurationService;
        this.enforcementEngine = enforcementEngine;
        this.auditLayer = auditLayer;
        this.registry = new SubCommandRegistry();
        registerSubCommands();
    }

    @Override
    public void register() {
        PluginCommand command = plugin.getCommand("judexis");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                gui.openMainConsole(((Player) sender).getUniqueId());
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can open console.");
            }
            return true;
        }

        SubCommand subCommand = registry.get(args[0]);
        if (subCommand == null) {
            sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
            return true;
        }
        if (!sender.hasPermission(subCommand.permission())) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }
        return subCommand.execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("profile");
            completions.add("panic");
            completions.add("reload");
            completions.add("enforcement");
            completions.add("freeze");
            completions.add("unfreeze");
        }
        return completions;
    }

    private void registerSubCommands() {
        registry.register(new SubCommand() {
            @Override
            public String name() {
                return "profile";
            }

            @Override
            public String permission() {
                return "judexis.profile";
            }

            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /judexis profile <safe|balanced|strict>");
                    return true;
                }
                try {
                    DetectionProfile profile = DetectionProfile.valueOf(args[1].toUpperCase());
                    profileEngine.switchProfile(profile);
                    sender.sendMessage(ChatColor.GREEN + "Profile switched to " + profile.name());
                    auditLayer.log("CMD_PROFILE", sender instanceof Player ? ((Player) sender).getUniqueId() : new java.util.UUID(0L, 0L), profile.name(), true);
                } catch (IllegalArgumentException ex) {
                    sender.sendMessage(ChatColor.RED + "Invalid profile.");
                }
                return true;
            }
        });

        registry.register(new SubCommand() {
            @Override
            public String name() {
                return "panic";
            }

            @Override
            public String permission() {
                return "judexis.panic";
            }

            @Override
            public boolean execute(CommandSender sender, String[] args) {
                boolean enabled = !profileEngine.panicMode();
                profileEngine.setPanicMode(enabled);
                sender.sendMessage(enabled ? ChatColor.YELLOW + "Panic mode enabled." : ChatColor.GREEN + "Panic mode disabled.");
                auditLayer.log("CMD_PANIC", sender instanceof Player ? ((Player) sender).getUniqueId() : new java.util.UUID(0L, 0L), Boolean.toString(enabled), true);
                return true;
            }
        });

        registry.register(new SubCommand() {
            @Override
            public String name() {
                return "reload";
            }

            @Override
            public String permission() {
                return "judexis.owner";
            }

            @Override
            public boolean execute(CommandSender sender, String[] args) {
                configurationService.reload();
                sender.sendMessage(ChatColor.GREEN + "Judexis config reloaded.");
                auditLayer.log("CMD_RELOAD", sender instanceof Player ? ((Player) sender).getUniqueId() : new java.util.UUID(0L, 0L), "config", true);
                return true;
            }
        });

        registry.register(new SubCommand() {
            @Override
            public String name() {
                return "enforcement";
            }

            @Override
            public String permission() {
                return "judexis.owner";
            }

            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /judexis enforcement <on|off>");
                    return true;
                }
                boolean enabled = "on".equalsIgnoreCase(args[1]);
                enforcementEngine.setEnforcementEnabled(enabled);
                sender.sendMessage(enabled ? ChatColor.GREEN + "Enforcement enabled." : ChatColor.YELLOW + "Enforcement disabled.");
                auditLayer.log("CMD_ENFORCEMENT", sender instanceof Player ? ((Player) sender).getUniqueId() : new java.util.UUID(0L, 0L), Boolean.toString(enabled), true);
                return true;
            }
        });

        registry.register(new SubCommand() {
            @Override
            public String name() {
                return "freeze";
            }

            @Override
            public String permission() {
                return "judexis.owner";
            }

            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /judexis freeze <player>");
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                enforcementEngine.freeze(target.getUniqueId(), sender.getName());
                sender.sendMessage(ChatColor.GREEN + "Player frozen.");
                return true;
            }
        });

        registry.register(new SubCommand() {
            @Override
            public String name() {
                return "unfreeze";
            }

            @Override
            public String permission() {
                return "judexis.owner";
            }

            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /judexis unfreeze <player>");
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                enforcementEngine.unfreeze(target.getUniqueId(), sender.getName());
                sender.sendMessage(ChatColor.GREEN + "Player unfrozen.");
                return true;
            }
        });
    }
}
