package me.danielskrob.minipixelAPI.placeholders;

import org.bukkit.OfflinePlayer;

import java.util.function.Function;
import java.util.function.Supplier;

public class PlaceholderManager {

    private static PlaceholderUtils instance;

    public static void init() {
        instance = new PlaceholderUtils();
        instance.register(); // You can make this auto-register defaults if you want
    }

    public static void register(String key, Supplier<String> supplier) {
        if (instance != null) instance.register(key, supplier);
    }

    public static void register(String key, Function<OfflinePlayer, String> function) {
        if (instance != null) instance.register(key, function);
    }
}
