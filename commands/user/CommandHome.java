package me.nostyll.Kingdoms.levels.commands.user;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandHome{

    public void reloadFiles(final CommandSender commandSender) {
    	
    	commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&l(!) &eTrying to sent you home!"));
    }
	
}
