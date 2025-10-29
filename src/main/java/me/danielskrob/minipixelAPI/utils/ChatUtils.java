package me.danielskrob.minipixelAPI.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ChatUtils {

    public static Component outputMessage(String message) {
        String miniMsg = message.replaceAll("&#([A-Fa-f0-9]{6})", "<#$1>");
        return MiniMessage.miniMessage().deserialize(miniMsg);
    }
}
