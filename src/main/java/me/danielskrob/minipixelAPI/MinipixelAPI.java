package me.danielskrob.minipixelAPI;

import me.danielskrob.minipixelAPI.placeholders.PlaceholderManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinipixelAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        PlaceholderManager.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
