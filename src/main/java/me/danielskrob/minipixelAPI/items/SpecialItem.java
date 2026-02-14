package me.danielskrob.minipixelAPI.items;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.danielskrob.minipixelAPI.utils.ChatUtils;
import me.danielskrob.minipixelAPI.utils.ItemManager;
import me.danielskrob.minipixelAPI.utils.SpectatorManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class SpecialItem {

    public SpecialItem(Plugin plugin) {
        this.plugin = plugin;

        ItemManager.items.put(getId(), this);
    }

    final private Plugin plugin;

    private static final Map<ArmorStand, SpawnedItemData> activeItems = new HashMap<>();

    private static class SpawnedItemData {
        private final Hologram hologram;
        private final SpecialItem item;

        private SpawnedItemData(Hologram hologram, SpecialItem item) {
            this.hologram = hologram;
            this.item = item;
        }
    }

    public abstract String getId();
    public abstract Material getMaterial();
    public abstract String getName();
    public abstract List<String> getLore();
    public abstract String getFloatingName();

    public NamespacedKey getItemKey() {
        return new NamespacedKey(plugin, "special_item_type");
    }

    private ItemStack getItem() {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatUtils.translateColorCodes(getName()));
        meta.setLore(getLore().stream().map(ChatUtils::translateColorCodes).toList());

        meta.getPersistentDataContainer().set(getItemKey(), PersistentDataType.STRING, getId().toLowerCase());
        item.setItemMeta(meta);
        return item;
    }

    public void spawnItem(Location location) {

        ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
        stand.setInvisible(true);
        stand.setGravity(false);
        stand.setMarker(true);
        stand.setInvulnerable(true);

        Hologram hologram = DHAPI.createHologram(UUID.randomUUID().toString(),
                location.clone().add(0, 1.0, 0));

        DHAPI.addHologramLine(hologram, getFloatingName());
        DHAPI.addHologramLine(hologram, getItem());

        activeItems.put(stand, new SpawnedItemData(hologram, this));
    }

    public static void startPickupTask(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {

                Iterator<Map.Entry<ArmorStand, SpawnedItemData>> it =
                        activeItems.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry<ArmorStand, SpawnedItemData> entry = it.next();

                    ArmorStand stand = entry.getKey();
                    SpawnedItemData data = entry.getValue();

                    if (!stand.isValid()) {
                        data.hologram.delete();
                        it.remove();
                        continue;
                    }

                    for (Player player : stand.getWorld().getPlayers()) {

                        if (SpectatorManager.isSpectator(player)) continue;

                        if (player.getLocation().distanceSquared(
                                stand.getLocation()) <= 1.44) {

                            player.getInventory().addItem(data.item.getItem());

                            player.playSound(player.getLocation(),
                                    Sound.ENTITY_ITEM_PICKUP, 1f, 1.2f);

                            player.getWorld().spawnParticle(
                                    Particle.CLOUD,
                                    stand.getLocation().clone().add(0, 0.5, 0),
                                    5, 0.1, 0.1, 0.1, 0.05
                            );

                            stand.remove();
                            data.hologram.delete();
                            it.remove();
                            break;
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}
