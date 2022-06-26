package me.nostyll.Kingdoms.levels.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.land.Land;
import org.kingdoms.constants.land.structures.Structure;
import org.kingdoms.events.lands.UnclaimLandEvent;
import org.kingdoms.constants.player.KingdomPlayer;

import com.cryptomorin.xseries.messages.Titles;
import com.cryptomorin.xseries.XMaterial;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.gui.level.LevelMenuGui;
import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;
import me.nostyll.Kingdoms.levels.handler.levels.LevelsConfigLoader;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;

public class LevelNexusManager extends Manager implements Listener{
	
	String nexuslevel = "nexuslevel";
	LevelsConfigLoader levelsloader = LevelsKingdoms.getLevelsConfigLoader();

	protected LevelNexusManager(Plugin plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onLevelClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		XMaterial mat = null;

		if (levelsloader.getConfig(1).getIcon() != null) mat = levelsloader.getConfig(1).getIcon();
		// Was a client side block.
		if (block == null) return;
		
		KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer(player.getUniqueId());
		if (mat != null && block.getType() == mat.parseMaterial() && block.hasMetadata(nexuslevel)) {
	        if (event.getAction() == Action.LEFT_CLICK_BLOCK) event.setCancelled(true);
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					 if (player.getGameMode() == GameMode.CREATIVE) return;
					Land land = Land.getLand(block.getLocation());
					Kingdom kingdom = kingdomPlayer.getKingdom();
					KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player.getUniqueId());
					if (kingdom != null && land.isClaimed() && land.getKingdom().getName().equals(kingdom.getName())) {
						LoadKingdoms loadedKingdoms = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
						if (!GameManagement.getKingdomManager().getLoadKingdomsData().hasKingdomData(kingdom.getId())) return;
						if (land.getKingdom().getName().equals(kingdom.getName())){
							if (loadedKingdoms.getlevel() == 1 && loadedKingdoms.getUgradeNextLevel()){
								Titles.clearTitle(player);
								Titles.sendTitle(player, 
										5, (3 * 20), 20, 
										ChatColor.DARK_RED + "" + ChatColor.BOLD + "ERROR!",
										ChatColor.WHITE + "" + ChatColor.BOLD + "This nexus is already running in upgrade mode!");
								Bukkit.getScheduler().runTaskLater(LevelsKingdoms.getInstance(), () -> {
									GameManagement.getLevelUpManager().getUpgradeMsg(player);
									return;
								}, 4 * 20);
							}
				    		LevelMenuGui.levelmenu(player, kp, kingdom).openInventory();
				    		event.setCancelled(true);
				    		return;
						}
					}
				}
			}
		
	}
	
	@EventHandler
	public void onLevelBlockBreakUnNatural(BlockBreakEvent event) {
		Block block = event.getBlock();
		
        XMaterial mat = levelsloader.getConfig(1).getIcon();
        final Material material = mat.parseMaterial();
		
		if (event.isCancelled())
			return;
		if (block.getType() != material)
			return;
		if (!block.hasMetadata(nexuslevel))
			return;
		event.setCancelled(true);
	}
	
	
	
	@EventHandler
	public void unclaimRemoveLevels(UnclaimLandEvent event){
		if (event.getLand() == null) return;
		Land land = event.getLand();
		Kingdom kingdom = event.getKingdomPlayer().getKingdom();
		Structure sturucter = land.getStructures().get(kingdom.getNexus());
		boolean nexus = sturucter.getStyle().getType().isNexus();
		if (nexus){
			if (GameManagement.getLevelUpManager().getLevel(kingdom) >= 1){
				GameManagement.getLevelUpManager().removeHoloAndLevel(kingdom);	
			}
		}
	}
	
	@Override
	public void onDisable() {
		
	}

}
