package me.danielskrob.minipixelAPI.menus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the inventory belongs to our Menu system
        if (event.getInventory().getHolder() instanceof BaseMenu menu) {
            event.setCancelled(true); // Prevent players from stealing items

            if (event.getClickedInventory() != null &&
                    event.getClickedInventory().equals(event.getInventory())) {
                menu.handleTrigger(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof BaseMenu menu) {
            menu.onMenuClose(event);
        }
    }
}