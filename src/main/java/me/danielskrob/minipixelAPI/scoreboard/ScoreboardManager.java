package me.danielskrob.minipixelAPI.scoreboard;

import fr.mrmicky.fastboard.adventure.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ScoreboardManager {
    private static final Map<UUID, FastBoard> boards = new HashMap<>();
    private static final List<String> lines = new ArrayList<>();
    private static String title;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static void setTitle(String name) {
        title = name;
        updateAllTitles();
    }

    public static void setLines(List<String> newLines) {
        lines.clear();
        lines.addAll(newLines);
        updateAllLines();
    }

    public static void updateLine(int index, String value) {
        lines.set(index, value);
        updateAllLines();
    }

    public static void addLine(String value) {
        lines.add(value);
        updateAllLines();
    }

    public static void removeLine(int index) {
        lines.remove(index);
        updateAllLines();
    }

    public static void showTo(Player player) {
        FastBoard board = new FastBoard(player);
        board.updateTitle(parse(player, title));
        board.updateLines(parseLines(player));
        boards.put(player.getUniqueId(), board);
    }

    public static void hideFrom(Player player) {
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) board.delete();
    }

    public static void updateAllTitles() {
        for (Map.Entry<UUID, FastBoard> entry : boards.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            entry.getValue().updateTitle(parse(player, title));
        }
    }

    public static void updateAllLines() {
        for (Map.Entry<UUID, FastBoard> entry : boards.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            entry.getValue().updateLines(parseLines(player));
        }
    }

    public static void clearAll() {
        for (FastBoard b : boards.values()) b.delete();
        boards.clear();
    }

    private static List<Component> parseLines(Player player) {
        return lines.stream()
                .map(line -> parse(player, line))
                .toList();
    }

    private static Component parse(Player player, String text) {
        String withPlaceholders = PlaceholderAPI.setPlaceholders(player, text);
        return miniMessage.deserialize(withPlaceholders);
    }
}