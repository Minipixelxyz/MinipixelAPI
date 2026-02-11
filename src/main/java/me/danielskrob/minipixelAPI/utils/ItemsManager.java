package me.danielskrob.minipixelAPI.utils;

import me.danielskrob.minipixelAPI.items.SpecialItem;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import java.util.HashMap;
import java.util.Map;

public class ItemsManager {

    public ItemsManager(Plugin plugin) {
        this.plugin = plugin;
    }

    private final Plugin plugin;
    public static Map<String, SpecialItem> items = new HashMap<>();

    public SpecialItem getFromItem(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) return null;

        NamespacedKey key = new NamespacedKey(plugin, "special_item_type");
        String id = stack.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);

        return id != null ? items.get(id) : null;
    }
}
