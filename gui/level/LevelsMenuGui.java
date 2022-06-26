package me.nostyll.Kingdoms.levels.gui.level;

import java.util.stream.IntStream;

import com.cryptomorin.xseries.XMaterial;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.DefaultKingdomPermission;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.gui.GUIParser;
import org.kingdoms.gui.InteractiveGUI;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.game.GameManagement;
import me.nostyll.Kingdoms.levels.gui.Menus;
import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;
import me.nostyll.Kingdoms.levels.handler.levels.LevelsConfigLoader;
import net.md_5.bungee.api.ChatColor;

public class LevelsMenuGui {

    public static InteractiveGUI openLevelUp(final Player player, final KingdomPlayer kp, final Kingdom kingdom) {
    	LoadKingdoms loadedKingdoms = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
    	int level = loadedKingdoms.getlevel();
    	
    	LevelsConfigLoader levelsloader = LevelsKingdoms.getLevelsConfigLoader();
    	
    	final OfflinePlayer placeholder = kp.getOfflinePlayer();
    	
		if (!kp.hasPermission(DefaultKingdomPermission.NEXUS)) {
			DefaultKingdomPermission.NEXUS.sendDeniedMessage(player);
			return null;
		}
        final InteractiveGUI gui = GUIParser.parse(player, placeholder, Menus.LEVELS_UP.getMenuName(), new Object[0]);
        if (gui == null) {
            return null;
        }

		InteractiveGUI.OptionBuilder levelIcon = gui.getReusableOption("level");

        gui.push("back", () -> {
        	LevelMenuGui.levelmenu(player, kp, kingdom);
        });
        
		int MaxLevelFiles = LevelsKingdoms.getMax();
		
		IntStream.rangeClosed(0, MaxLevelFiles)
		.map(Level -> Level + 1)
		.limit(MaxLevelFiles)
		.forEach((Levels) -> {
				levelIcon.modifyItem(item ->  {
					levelsloader.getConfig(Levels).getIcon().setType(item);
				});
				levelIcon.push(()-> {
					
				} ,new Object[]{"status", LevelsMenuGui.checkLevelDone(Levels, loadedKingdoms), "level_item", Levels ,"level", level, "artifect", level == Levels && !loadedKingdoms.getCanUseItem() && !loadedKingdoms.getUgradeNextLevel() ? ChatColor.DARK_GREEN + "Running" : ChatColor.DARK_RED + "Not Running"} );
	       // gui.push("level" + Levels, () -> {
	            //if (loadedKingdoms.getlevel() == 1 && loadedKingdoms.getCanUseItem() && !loadedKingdoms.getUgradeNextLevel()) {
	               // if (kingdom.getResourcePoints() < levelsloader.getConfig(2).getLevelRpCost()) return;
						//kingdom.setResourcePoints(kingdom.getResourcePoints() - levelsloader.getConfig(2).getLevelRpCost());
						//loadedKingdoms.hologram().stopTask();
						//loadedKingdoms.canUseItem(false); 
	                    //loadedKingdoms.CanUgradeNextLevel(false);
	                    //GameManagement.getLevelUpManager().CreateLevelup((Player)player, kingdom);
	                    //((Player)player).closeInventory();
	            //}
	           // return;
	       // }, "%status%", LevelsMenuGui.checkLevelDone(Levels, loadedKingdoms), "%level%", level, "%artifect%", level == Levels && !loadedKingdoms.getCanUseItem() && !loadedKingdoms.getUgradeNextLevel() ? ChatColor.DARK_GREEN + "Running" : ChatColor.DARK_RED + "Not Running");
				}
			);
        gui.openInventory();
        return gui;
    }
    
    public void openBook(){
    	
    }
    
    public static String checkLevelDone(int level, LoadKingdoms loadedKingdoms){
    	int levelnow = loadedKingdoms.getlevel();
    	if (levelnow > level){
    		return ChatColor.DARK_GREEN +  "Done";
    	}else if (levelnow == level){
    		return ChatColor.DARK_AQUA + "Upgradeable";
    	}else{
    		return ChatColor.DARK_RED + "Not Upgradeable";
    	}
    }
    
}
