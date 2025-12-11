package me.danielskrob.minipixelAPI;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CommandUtil {
    public static void registerCommand(Plugin plugin, String name, CommandExecutor executor) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            Constructor<PluginCommand> constructor =
                    PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand command = constructor.newInstance(name, plugin);

            command.setExecutor(executor);
            command.setDescription("Dynamic command " + name);
            command.setUsage("/" + name);
            command.setPermissionMessage("You don't have permission to use this command!");

            commandMap.register(plugin.getName(), command);

            plugin.getLogger().info("Registered command: /" + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
