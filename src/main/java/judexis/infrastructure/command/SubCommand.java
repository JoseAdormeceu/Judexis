package judexis.infrastructure.command;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    String name();
    String permission();
    boolean execute(CommandSender sender, String[] args);
}
