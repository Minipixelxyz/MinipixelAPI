package me.danielskrob.minipixelAPI.command_lock;

import me.danielskrob.minipixelAPI.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandLockEvents implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (CommandLockManager.getPermittedPlayers().contains(player.getUniqueId())) {
            return;
        }

        // Raw message looks like "/tp Daniel" -> we want "tp"
        String rawMessage = event.getMessage().toLowerCase();
        String commandLabel = rawMessage.split(" ")[0].replace("/", "");

        if (!CommandLockManager.getPermittedCommands().contains(commandLabel)) {
            event.setCancelled(true);

            Bukkit.broadcastMessage("");
            Bukkit.broadcast(ChatUtils.outputMessage("<red>Takhle si Gala férové hraní nepředstavuje :("));
            Bukkit.broadcast(ChatUtils.outputMessage("<red>" + player.getName() + " <red><bold>TRIED TO CHEAT <reset><red>with command: " + rawMessage));
            Bukkit.broadcastMessage("");
        }
    }
}