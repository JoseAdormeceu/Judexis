package io.judexis.adapter.v1_8.command;

import io.judexis.adapter.v1_8.session.SessionManager;
import io.judexis.core.JudexisCoreEngine;
import io.judexis.core.debug.CoreDebugSnapshot;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.violation.Evidence;
import io.judexis.core.violation.EvidenceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * Runtime debug and check-toggle command.
 */
public final class JudexisCommand implements CommandExecutor {
    private final JudexisCoreEngine coreEngine;
    private final SessionManager sessionManager;

    public JudexisCommand(JudexisCoreEngine coreEngine, SessionManager sessionManager) {
        this.coreEngine = coreEngine;
        this.sessionManager = sessionManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /judexis <debug|toggle>");
            return true;
        }
        if ("debug".equalsIgnoreCase(args[0])) {
            return handleDebug(sender, args);
        }
        if ("toggle".equalsIgnoreCase(args[0])) {
            return handleToggle(sender, args);
        }
        sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
        return true;
    }

    private boolean handleDebug(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /judexis debug <player>");
            return true;
        }
        Player player = Bukkit.getPlayerExact(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player is not online.");
            return true;
        }
        PlayerProfile profile = sessionManager.get(player.getUniqueId());
        if (profile == null) {
            sender.sendMessage(ChatColor.YELLOW + "Player is online but not tracked by Judexis.");
            return true;
        }
        CoreDebugSnapshot snapshot = coreEngine.debug(profile, 10);
        if (snapshot == null) {
            sender.sendMessage(ChatColor.YELLOW + "No data available for player.");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "Judexis debug for " + player.getName());
        sender.sendMessage(ChatColor.GRAY + "Ping: " + snapshot.getPingEstimateMillis() + "ms"
            + " | TPS: " + String.format("%.2f", snapshot.getTicksPerSecond()));
        sender.sendMessage(ChatColor.GRAY + "JoinTicks: " + snapshot.getJoinTicks()
            + " | TeleportTicks: " + snapshot.getTeleportTicks()
            + " | VelocityTicks: " + snapshot.getVelocityTicks());

        Map<EvidenceType, Double> categories = snapshot.getViolationByCategory();
        sender.sendMessage(ChatColor.AQUA + "Violations by category:");
        for (EvidenceType type : EvidenceType.values()) {
            Double score = categories.get(type);
            sender.sendMessage(ChatColor.DARK_AQUA + " - " + type.name() + ": "
                + String.format("%.2f", score == null ? 0.0D : score.doubleValue()));
        }

        List<Evidence> evidence = snapshot.getRecentEvidence();
        sender.sendMessage(ChatColor.AQUA + "Recent evidence (max 10):");
        for (int i = 0; i < evidence.size(); i++) {
            Evidence entry = evidence.get(i);
            long millis = entry.getCapturedAtNanos() / 1_000_000L;
            sender.sendMessage(ChatColor.DARK_GRAY + " - [" + millis + "] "
                + entry.getCheckName() + " " + entry.getType().name() + " "
                + entry.getSeverity().name() + " " + entry.getDetail());
        }
        return true;
    }

    private boolean handleToggle(CommandSender sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Only operators can toggle checks.");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /judexis toggle <checkId> <on|off>");
            return true;
        }
        String checkId = args[1];
        if (!coreEngine.isCheckRegistered(checkId)) {
            sender.sendMessage(ChatColor.RED + "Unknown check id: " + checkId);
            return true;
        }

        boolean enable;
        if ("on".equalsIgnoreCase(args[2])) {
            enable = true;
        } else if ("off".equalsIgnoreCase(args[2])) {
            enable = false;
        } else {
            sender.sendMessage(ChatColor.RED + "State must be on or off.");
            return true;
        }

        coreEngine.setCheckEnabled(checkId, enable);
        sender.sendMessage(ChatColor.GREEN + "Check " + checkId + " is now " + (enable ? "enabled" : "disabled") + ".");
        return true;
    }
}
