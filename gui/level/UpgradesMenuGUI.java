package me.nostyll.Kingdoms.levels.gui.level;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.DefaultKingdomPermission;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.gui.GUIParser;
import org.kingdoms.gui.InteractiveGUI;

import me.nostyll.Kingdoms.levels.gui.Menus;

public class UpgradesMenuGUI {
	
	public static InteractiveGUI upgrademenu(final Player player, final KingdomPlayer kp, final Kingdom kingdom) {
    	OfflinePlayer placeholder = kp.getOfflinePlayer();
    	//LoadKingdoms loadedKingdoms = GameManagement.getKingdomManager().getKingdomData(kingdom.getId());
        if (!kp.hasPermission(DefaultKingdomPermission.NEXUS)) {
            DefaultKingdomPermission.NEXUS.sendDeniedMessage(player);
            return null;
        }
        else {
        	InteractiveGUI gui = GUIParser.parse(player, placeholder, Menus.UPGRADES.getMenuName(), new Object[0]);
            if (gui == null) {
                return null;
            }
            else {
            	gui.push("back", () -> {
            		LevelMenuGui.levelmenu(player, kp, kingdom);
            	});
                gui.openInventory();
                return gui;
            }
        }
}

}
