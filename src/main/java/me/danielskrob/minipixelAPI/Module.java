package me.danielskrob.minipixelAPI;

import org.bukkit.plugin.Plugin;

public interface Module {
    void onEnable(Plugin plugin, String data, CoreBridge bridge);
    String getName();
}
