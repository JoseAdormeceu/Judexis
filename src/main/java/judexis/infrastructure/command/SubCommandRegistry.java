package judexis.infrastructure.command;

import java.util.HashMap;
import java.util.Map;

public final class SubCommandRegistry {

    private final Map<String, SubCommand> commands;

    public SubCommandRegistry() {
        this.commands = new HashMap<String, SubCommand>();
    }

    public void register(SubCommand subCommand) {
        commands.put(subCommand.name().toLowerCase(), subCommand);
    }

    public SubCommand get(String name) {
        return commands.get(name.toLowerCase());
    }
}
