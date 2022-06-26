package me.nostyll.Kingdoms.levels.gui.discord;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.gui.GUIParser;
import org.kingdoms.gui.InteractiveGUI;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.external.ExternalManager;
import me.nostyll.Kingdoms.levels.gui.Menus;
import me.nostyll.Kingdoms.levels.gui.level.LevelMenuGui;
import me.spomg.plugins.api.minecord.MAPI;

public class DiscordMenuGui {

	MAPI minecord = LevelsKingdoms.getInstance().getServer().getServicesManager().load(MAPI.class);
	
	
	public static InteractiveGUI discordmenu(final Player player, final KingdomPlayer kp, final Kingdom kingdom) {
    	OfflinePlayer placeholder = kp.getOfflinePlayer();
    	//LoadKingdoms loadedKingdoms = GameManagement.getKingdomManager().getKingdomData(kingdom.getId());
        //DiscordKingdoms DiscordKingdoms = loadedKingdoms.getdiscordKingdoms();
        	InteractiveGUI gui = GUIParser.parse(player, placeholder, Menus.DISCORD.getMenuName(), new Object[0]);
            if (gui == null) {
                return null;
            }
            else {
                gui.push("discordmembers", () -> DiscordMembersGui.openMembers(kp, player, placeholder, kingdom, 0));
                gui.push("back", () -> LevelMenuGui.levelmenu(player, kp, kingdom));
                gui.openInventory();
                return gui;
            }
}
	
}
