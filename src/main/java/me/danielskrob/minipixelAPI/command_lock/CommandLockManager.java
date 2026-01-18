package me.danielskrob.minipixelAPI.command_lock;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CommandLockManager {
    private static final Set<String> permittedCommands = new HashSet<>();
    private static final Set<UUID> permittedPlayers = new HashSet<>();

    public static void allowCommand(String command){
       permittedCommands.add(command);
    }

    public static void allowPlayer(UUID player){
        permittedPlayers.add(player);
    }

    public static Set<String> getPermittedCommands() {
        return permittedCommands;
    }

    public static Set<UUID> getPermittedPlayers() {
        return permittedPlayers;
    }
}
