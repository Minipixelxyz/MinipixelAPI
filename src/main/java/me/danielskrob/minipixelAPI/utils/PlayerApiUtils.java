package me.danielskrob.minipixelAPI.utils;

import me.danielskrob.minipixelAPI.MinipixelAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerApiUtils {

    private static MinipixelAPI plugin;
    private static final Set<UUID> startingPlayers = new HashSet<>();

    public static void init(MinipixelAPI reference) {
        plugin = reference;
    }

    public static void spawnPlayer(Player player, Location location, int time) {
        if (time != 0){
            startingPlayers.add(player.getUniqueId());
            player.setInvulnerable(true);

            new BukkitRunnable() {
                int timer = time;

                @Override
                public void run() {
                    if (timer == 0){
                        Title title = Title.title(
                                MiniMessage.miniMessage().deserialize("<green><bold>GO!"),
                                Component.empty()
                        );
                        player.showTitle(title);

                        startingPlayers.remove(player.getUniqueId());
                        player.setInvulnerable(false);

                        cancel();
                        return;
                    }

                    Title title = Title.title(
                            MiniMessage.miniMessage().deserialize("<yellow><bold>" + timer),
                            Component.empty()
                    );
                    player.showTitle(title);

                    timer--;
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }

        player.teleport(location);
    }

    public static Set<UUID> getStartingPlayers() {
        return startingPlayers;
    }
}
