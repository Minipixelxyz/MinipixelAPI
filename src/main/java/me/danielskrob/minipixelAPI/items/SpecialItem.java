package me.danielskrob.minipixelAPI.items;

import me.danielskrob.minipixelAPI.utils.ChatUtils;
import me.danielskrob.minipixelAPI.utils.ItemManager;
import me.danielskrob.minipixelAPI.utils.SpectatorManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class SpecialItem {

    public SpecialItem(Plugin plugin) {
        this.plugin = plugin;

        ItemManager.items.put(getId(), this);
    }

    final private Plugin plugin;

    private static final List<ArmorStand> activeItems = new ArrayList<>();

    public abstract String getId();
    public abstract Material getMaterial();
    public abstract String getName();
    public abstract List<String> getLore();
    public abstract List<String> getFloatingLore();

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
        World world = location.getWorld();
        if (world == null) return;

        // 1. Center the item and raise it slightly so it doesn't clip the ground
        Location itemLoc = location.clone().add(0, 0.5, 0);

        ArmorStand itemStand = world.spawn(itemLoc, ArmorStand.class, as -> {
            as.setVisible(false);
            as.setGravity(false);
            as.setBasePlate(false);
            as.setSmall(false); // Keep the item itself big
            as.setInvulnerable(true);

            // This pose stands the item straight up vertically
            as.setRightArmPose(new EulerAngle(Math.toRadians(-90), Math.toRadians(0), Math.toRadians(0)));

            as.getEquipment().setItemInMainHand(getItem());
            as.addScoreboardTag("SpecialItemTag");
        });

        // 2. Setup the Floating Lore
        List<String> lore = new ArrayList<>(getFloatingLore());
        lore.add(0, getName()); // Name stays at the very top

        // Start slightly above the ArmorStand's head
        // Smaller increments (0.24) prevent that "stretched out" look
        double yOffset = 1.2;

        // Iterate in reverse to stack correctly: Bottom of lore list = bottom line in-game
        for (int i = lore.size() - 1; i >= 0; i--) {
            String line = lore.get(i);
            if (line.isEmpty()) continue;

            Location loreLoc = itemLoc.clone().add(0, yOffset, 0);
            world.spawn(loreLoc, ArmorStand.class, as -> {
                as.setMarker(true);
                as.setVisible(false);
                as.setGravity(false);
                as.setSmall(true); // Small stands make the text labels tighter
                as.setCustomNameVisible(true);
                as.setCustomName(ChatUtils.translateColorCodes(line));
                as.addScoreboardTag("SpecialItemLoreTag");
            });
            yOffset += 0.24; // Tight vertical spacing
        }

        activeItems.add(itemStand);
    }

    public static void startCollisionTask(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Iterator<ArmorStand> it = activeItems.iterator(); it.hasNext();) {
                    ArmorStand as = it.next();

                    if (!as.isValid()) {
                        it.remove();
                        continue;
                    }

                    if (!as.getScoreboardTags().contains("SpecialItemTag")) {
                        continue;
                    }

                    for (Entity entity : as.getNearbyEntities(1.2, 1.2, 1.2)) {
                        if (entity instanceof Player player) {
                            if (!SpectatorManager.isSpectator(player)) {

                                pickup(player, as);
                                it.remove();
                                break;
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    private static void pickup(Player player, ArmorStand as) {
        player.getInventory().addItem(as.getEquipment().getItemInMainHand());
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.2f);

        as.getNearbyEntities(1.0, 3.0, 1.0).stream()
                .filter(e -> e.getScoreboardTags().contains("SpecialItemLoreTag"))
                .forEach(Entity::remove);

        as.remove();
        as.getWorld().spawnParticle(Particle.CLOUD, as.getLocation().add(0, 0.5, 0), 5, 0.1, 0.1, 0.1, 0.05);
    }
}
