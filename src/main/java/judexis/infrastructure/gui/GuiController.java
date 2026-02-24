package judexis.infrastructure.gui;

import judexis.JudexisPlugin;
import judexis.core.audit.AuditLayer;
import judexis.core.gui.GuiControlSuite;
import judexis.core.profile.ProfileEngine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GuiController implements GuiControlSuite, Listener {

    private final JudexisPlugin plugin;
    private final ProfileEngine profileEngine;
    private final AuditLayer auditLayer;
    private final Map<UUID, Deque<String>> history;

    public GuiController(JudexisPlugin plugin, ProfileEngine profileEngine, AuditLayer auditLayer) {
        this.plugin = plugin;
        this.profileEngine = profileEngine;
        this.auditLayer = auditLayer;
        this.history = new HashMap<UUID, Deque<String>>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void openMainConsole(UUID viewerId) {
        Player viewer = Bukkit.getPlayer(viewerId);
        if (viewer == null) {
            return;
        }
        Inventory inventory = Bukkit.createInventory(new RoutingInventoryHolder("MAIN"), 27, ChatColor.DARK_GRAY + "Judexis Console");
        if (viewer.hasPermission("judexis.console")) {
            inventory.setItem(11, item(Material.REDSTONE_COMPARATOR, ChatColor.AQUA + "System Diagnostics"));
            inventory.setItem(13, item(Material.BOOK, ChatColor.AQUA + "Player Forensic"));
        }
        if (viewer.hasPermission("judexis.owner")) {
            inventory.setItem(15, item(Material.COMMAND, ChatColor.AQUA + "Owner Controls"));
        }
        push(viewerId, "MAIN");
        viewer.openInventory(inventory);
    }

    @Override
    public void openForensic(UUID viewerId, UUID targetId) {
        Player viewer = Bukkit.getPlayer(viewerId);
        if (viewer == null) {
            return;
        }
        Inventory inventory = Bukkit.createInventory(new RoutingInventoryHolder("FORENSIC"), 27, ChatColor.DARK_GRAY + "Forensic Panel");
        inventory.setItem(13, item(Material.SKULL_ITEM, ChatColor.YELLOW + "Target: " + targetId.toString()));
        push(viewerId, "FORENSIC");
        viewer.openInventory(inventory);
    }

    @Override
    public void back(UUID viewerId) {
        Deque<String> stack = history.computeIfAbsent(viewerId, ignored -> new ArrayDeque<String>(8));
        if (stack.size() <= 1) {
            openMainConsole(viewerId);
            return;
        }
        stack.removeLast();
        String route = stack.peekLast();
        if ("MAIN".equals(route)) {
            openMainConsole(viewerId);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof RoutingInventoryHolder)) {
            return;
        }

        event.setCancelled(true);
        RoutingInventoryHolder holder = (RoutingInventoryHolder) event.getInventory().getHolder();
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }

        if ("MAIN".equals(holder.routeKey())) {
            if (item.getType() == Material.BOOK) {
                openForensic(player.getUniqueId(), player.getUniqueId());
                auditLayer.log("GUI_FORENSIC_OPEN", player.getUniqueId(), player.getName(), true);
            } else if (item.getType() == Material.REDSTONE_COMPARATOR) {
                player.sendMessage(ChatColor.GRAY + "Profile: " + profileEngine.activeProfile().name());
            }
        }
    }

    private void push(UUID viewer, String route) {
        Deque<String> stack = history.computeIfAbsent(viewer, ignored -> new ArrayDeque<String>(8));
        if (stack.isEmpty() || !route.equals(stack.peekLast())) {
            stack.addLast(route);
        }
    }

    private ItemStack item(Material material, String name) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            stack.setItemMeta(meta);
        }
        return stack;
    }
}
