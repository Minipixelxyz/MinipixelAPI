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
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            viewer.hidePlayer(plugin, target);
        }

        spectators.add(target.getUniqueId());
        target.setGameMode(GameMode.ADVENTURE);
        target.setInvulnerable(true);
        target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1, false, false));
    }

    public static void showToEveryone(Player target) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            viewer.showPlayer(plugin, target);
        }

        spectators.remove(target.getUniqueId());
        target.setInvulnerable(false);
    }

    public static Set<UUID> getSpectators() {
        return spectators;
    }
}