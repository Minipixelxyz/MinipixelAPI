package me.danielskrob.minipixelAPI.menus;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseMenu implements InventoryHolder {

    protected final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> actions = new HashMap<>();

    public BaseMenu(int size, String title) {
        this.inventory = Bukkit.createInventory(this, size, title);
    }

    // Add an item and define what it does when clicked
    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> action) {
        inventory.setItem(slot, item);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    // Called by the Listener
    public void handleTrigger(InventoryClickEvent event) {
        Consumer<InventoryClickEvent> action = actions.get(event.getRawSlot());
        if (action != null) {
            action.accept(event);
        }
    }

    // Override this in your menu subclasses
    public void onMenuClose(InventoryCloseEvent event) {}

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}