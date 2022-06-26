package me.nostyll.Kingdoms.levels.utils;

import org.bukkit.entity.*;
import org.bukkit.command.*;
import org.bukkit.*;
import java.util.*;
import java.text.*;

public class Chat
{
    public static String color(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
    public static void msg(final Player player, final String... messages) {
        Arrays.stream(messages).forEach(s -> player.sendMessage(color(s)));
    }
    
    public static void msg(final CommandSender sender, final String... messages) {
        Arrays.stream(messages).forEach(s -> sender.sendMessage(color(s)));
    }
    
    public static void msgAll(final String... messages) {
        Bukkit.getOnlinePlayers().stream().forEach(o -> Arrays.stream(messages).forEach(s -> o.sendMessage(color(s))));
    }
    
    public static void log(final String message) {
        Bukkit.getConsoleSender().sendMessage(color(message));
    }
    
    public static String uppercaseFirst(final String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length()).toLowerCase();
    }
    
    public static int getRandomNumber(final int min, final int max) {
        return new Random().nextInt(max - min + 1);
    }
    
    public static String formatMs(final long ms) {
        final long n = ms / 1000L % 60L;
        final long n2 = ms / 60000L % 60L;
        final long n3 = ms / 3600000L % 24L;
        return ((n3 > 0L) ? (n3 + "h ") : "") + ((n2 > 0L) ? (n2 + "m ") : "") + n + "s";
    }
    
    public static String timeLeft(final long timeoutSeconds) {
        final long n = timeoutSeconds / 86400L;
        final long n2 = timeoutSeconds / 3600L % 24L;
        final long n3 = timeoutSeconds / 60L % 60L;
        final long n4 = timeoutSeconds % 60L;
        return ((n > 0L) ? (" " + n + "d" + ((n != 1L) ? "" : "")) : "") + ((n2 > 0L) ? (" " + n2 + "h" + ((n2 != 1L) ? "" : "")) : "") + ((n3 > 0L) ? (" " + n3 + "m" + ((n3 != 1L) ? "" : "")) : "") + ((n4 > 0L) ? (" " + n4 + "s" + ((n4 != 1L) ? "" : "")) : "");
    }
    
    public static String formatDoubleValue(final double value) {
        final NumberFormat instance = NumberFormat.getInstance(Locale.ENGLISH);
        instance.setMaximumFractionDigits(2);
        instance.setMinimumFractionDigits(2);
        return instance.format(value);
    }

    
    public static String formatText(String text) {
        if (text == null || text.equals(""))
            return "";
        return formatText(text, false);
    }

    public static String formatText(String text, boolean cap) {
        if (text == null || text.equals(""))
            return "";
        if (cap)
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String convertToInvisibleString(String s) {
        if (s == null || s.equals(""))
            return "";
        StringBuilder hidden = new StringBuilder();
        for (char c : s.toCharArray()) hidden.append(ChatColor.COLOR_CHAR + "").append(c);
        return hidden.toString();
    }
}
