package judexis.infrastructure.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class RoutingInventoryHolder implements InventoryHolder {

    private final String routeKey;

    public RoutingInventoryHolder(String routeKey) {
        this.routeKey = routeKey;
    }

    public String routeKey() {
        return routeKey;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
