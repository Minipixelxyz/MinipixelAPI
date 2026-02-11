package me.danielskrob.minipixelAPI.items;

import me.danielskrob.minipixelAPI.utils.SpectatorManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class SpecialItem {
    private static final List<ArmorStand> activeItems = new ArrayList<>();

    public abstract String getName();
    public abstract String getDescription();
    public abstract ItemStack getItemStack();
    public abstract ChatColor getColor();

    private ItemStack getItem() {

        ItemStack item = getItemStack();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>(List.of(getDescription()));

        meta.setDisplayName(getColor() + getName());
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public void spawnItem(Location location) {

        ArmorStand armorStand =  (ArmorStand) location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0)));

        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(getColor() + getName());
        armorStand.getEquipment().setItemInMainHand(getItem());
        armorStand.setSmall(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.addScoreboardTag("SpecialItemTag");

        activeItems.add(armorStand);

    }

    public static void startCollisionTask(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Iterator<ArmorStand> it = activeItems.iterator(); it.hasNext(); ) {
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

        player.getInventory().addItem(as.getEquipment().getItemInHand());
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.2f);
        as.remove();

        as.getWorld().spawnParticle(Particle.CLOUD, as.getLocation().add(0, 0.5, 0), 5, 0.1, 0.1, 0.1, 0.05);
    }
}
