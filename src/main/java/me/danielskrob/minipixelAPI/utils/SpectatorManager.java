package me.danielskrob.minipixelAPI.utils;

import me.danielskrob.minipixelAPI.MinipixelAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpectatorManager {

    private static MinipixelAPI plugin;
    private static final Set<UUID> spectators = new HashSet<>();

    public static void init(MinipixelAPI api) {
        plugin = api;
    }

    public static void hideFromEveryone(Player target) {
        UUID targetId = target.getUniqueId();
        spectators.add(targetId);

        for (Player viewer : Bukkit.getOnlinePlayers()) {
            boolean viewerIsSpectator = spectators.contains(viewer.getUniqueId());

            if (viewerIsSpectator) {
                viewer.showPlayer(plugin, target);
            } else {
                viewer.hidePlayer(plugin, target);
            }
        }

        target.setGameMode(GameMode.ADVENTURE);
        target.setInvulnerable(true);
        target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

        target.setAllowFlight(true);
        target.setFlying(true);
    }

    public static void showToEveryone(Player target) {
        UUID targetId = target.getUniqueId();
        spectators.remove(targetId);

        for (Player viewer : Bukkit.getOnlinePlayers()) {
            viewer.showPlayer(plugin, target);
        }

        target.setInvulnerable(false);
        target.removePotionEffect(PotionEffectType.INVISIBILITY);

        target.setAllowFlight(false);
        target.setFlying(false);
    }

    public static Set<UUID> getSpectators() {
        return spectators;
    }

    public static boolean isSpectator(Player player) {
        return spectators.contains(player.getUniqueId());
    }
}