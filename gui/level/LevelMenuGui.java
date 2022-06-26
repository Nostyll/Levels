package me.nostyll.Kingdoms.levels.gui.level;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.DefaultKingdomPermission;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.gui.GUIParser;
import org.kingdoms.gui.InteractiveGUI;
import org.kingdoms.main.locale.MessageHandler;

import me.nostyll.Kingdoms.levels.game.GameManagement;
import me.nostyll.Kingdoms.levels.gui.Menus;
import me.nostyll.Kingdoms.levels.gui.discord.DiscordMenuGui;
import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;

public class LevelMenuGui {
	
	public static InteractiveGUI levelmenu(final Player player, final KingdomPlayer kp, final Kingdom kingdom) {
        	OfflinePlayer placeholder = kp.getOfflinePlayer();
        	LoadKingdoms loadedKingdoms = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
            if (!kp.hasPermission(DefaultKingdomPermission.NEXUS)) {
                DefaultKingdomPermission.NEXUS.sendDeniedMessage(player);
                return null;
            }
            else {
            	InteractiveGUI gui = GUIParser.parse(player, placeholder, Menus.LEVELS.getMenuName(), new Object[0]);
                if (gui == null) {
                    return null;
                }
                else {
                    gui.push("discord", () -> DiscordMenuGui.discordmenu(player, kp, kingdom));
                    gui.push("levelup", () -> LevelsMenuGui.openLevelUp(player, kp, kingdom), new Object[] { "level", MessageHandler.colorize("&b&l" + String.valueOf(loadedKingdoms.getlevel())) });
                    gui.push("upgrades", ()-> {UpgradesMenuGUI.upgrademenu(player, kp, kingdom);});
                    gui.openInventory();
                    return gui;
                }
            }
	}
	
}
