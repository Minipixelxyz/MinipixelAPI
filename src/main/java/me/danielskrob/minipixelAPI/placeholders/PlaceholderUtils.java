package me.danielskrob.minipixelAPI.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class PlaceholderUtils extends PlaceholderExpansion {

    private final Map<String, Supplier<String>> globalPlaceholders = new HashMap<>();
    private final Map<String, Function<OfflinePlayer, String>> playerPlaceholders = new HashMap<>();

    /** Register a placeholder that doesn’t depend on player context */
    public void register(String key, Supplier<String> supplier) {
        globalPlaceholders.put(key.toLowerCase(), supplier);
    }

    /** Register a placeholder that depends on player context */
    public void register(String key, Function<OfflinePlayer, String> function) {
        playerPlaceholders.put(key.toLowerCase(), function);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "minipixelcore";
    }

    @Override
    public @NotNull String getAuthor() {
        return "danielskrob";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        params = params.toLowerCase();

        Function<OfflinePlayer, String> func = playerPlaceholders.get(params);
        if (func != null) {
            return func.apply(player);
        }

        Supplier<String> supplier = globalPlaceholders.get(params);
        if (supplier != null) {
            return supplier.get();
        }

        return null;
    }
}