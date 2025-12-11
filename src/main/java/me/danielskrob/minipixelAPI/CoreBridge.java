package me.danielskrob.minipixelAPI;

import org.bukkit.entity.Player;

import java.util.Set;

public interface CoreBridge {
    void endInstance();
    Set<Player> getActivePlayers();
    void endGameForPlayer(Player player, String message);
}
