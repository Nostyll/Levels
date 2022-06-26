package me.nostyll.Kingdoms.levels.game;

import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.land.Land;
import org.kingdoms.constants.land.location.SimpleLocation;
import org.kingdoms.constants.land.structures.Structure;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.utils.xseries.messages.ActionBar;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;
import me.nostyll.Kingdoms.levels.handler.levels.LevelsConfigLoader;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;
import me.nostyll.Kingdoms.levels.utils.Methods;

public class LevelUpItemManager extends Manager implements Listener{

	LevelsKingdoms plugin;
	LevelsConfigLoader levelsloader = LevelsKingdoms.getLevelsConfigLoader();

	int count;
    int last_indi;
    Timer timer;
    public boolean timer_state;
	
	public HashMap<UUID, Player> UpgradeMode = new HashMap<UUID, Player>();
	
	int MaxLevelFiles = LevelsKingdoms.config.maxLevels;
	
	protected LevelUpItemManager(Plugin plugin) {
		super(plugin);
	}

	public HashMap<UUID, Player> getUpgradeMode(){
		return this.UpgradeMode;
	}
	
	public void setUpgradeMode(UUID kingdomUUID, Player p){
		if (!getUpgradeMode().containsKey(kingdomUUID)){
			getUpgradeMode().put(kingdomUUID, p);	
		}
	}

	public void removeUpgradeMode(UUID kingdomUUID){
		getUpgradeMode().remove(kingdomUUID);
	}
	
	@EventHandler
	public void UseLevelsOne(PlayerInteractEvent event){
		Player player = event.getPlayer();
    	KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer(event.getPlayer().getUniqueId());
		if (kingdomPlayer.getKingdom() == null) return;

		XMaterial mat = levelsloader.getConfig(1).getIcon();
        
		Kingdom kingdom = kingdomPlayer.getKingdom();
		
		if (player.getGameMode() == GameMode.CREATIVE) return;
		if (kingdom.getNexus() == null) return;
		if (kingdomPlayer.isAdmin() && GameManagement.getadminManager().isUserEditing(player)) return;
		
		if (!GameManagement.getKingdomManager().getLoadKingdomsData().hasKingdomData(kingdom.getId())) return;
		Land land = Land.getLand(player.getLocation());
		if (kingdom != null && land.isClaimed() && land.getKingdom().getName().equalsIgnoreCase(kingdom.getName())) {		
			LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
			int time_one = 2 * (60 * 20);
			SimpleLocation nexus = kingdom.getNexus();
			
			if (event.getAction() == Action.RIGHT_CLICK_AIR && levelsloader.getConfig(1).getFinalItem().isSimilar(player.getInventory().getItemInMainHand())){
					
					if (kingdom.getResourcePoints() < levelsloader.getConfig(1).getLevelRpCost()){
						Titles.clearTitle(player);
						Titles.sendTitle(player, 
								5, (3 * 20), 20, 
								ChatColor.DARK_RED + "" + ChatColor.BOLD + "Error low ResourcePoints!",
								ChatColor.WHITE + "" + ChatColor.BOLD + "Your Kingdom need " + levelsloader.getConfig(1).getLevelRpCost() +" ResourcePoints");
    				return;
					}
					
					if (leveldata.getlevel() >= 1){
						Titles.clearTitle(player);
						Titles.sendTitle(player, 
								5, (3 * 20), 20, 
								ChatColor.DARK_RED + "" + ChatColor.BOLD + "ERROR!",
								ChatColor.WHITE + "" + ChatColor.BOLD + "Your nexus is already upgrade to a higher tier!");
						Bukkit.getScheduler().runTaskLater(LevelsKingdoms.getInstance(), () -> {
							Titles.clearTitle(player);
							Titles.sendTitle(player, 
									5, (3 * 20), 20, 
									ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Level Nexus",
									ChatColor.WHITE + "" + ChatColor.BOLD + "Use your level nexus to upgrade!");
						}, 4 * 20);
						return;
					}
					if (leveldata.getlevel() == 0){
						
						if (leveldata.getCanUseItem()){
							LevelsKingdoms.newChain()
					    	.sync(() -> {
								if (player.getInventory().getItemInMainHand().getAmount() > 1){
									player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);		
								}else{
									player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
								}
								leveldata.canUseItem(true);
								leveldata.hologram().startRotation(nexus.clone().toBukkitLocation().add(0.5, 1.5, 0.5), placeHologram(nexus.clone().toBukkitLocation().add(0.5, 1.5, 0.5), mat), time_one);
								ActionBar.clearActionBar(player);
					    		Titles.clearTitle(player);
								Titles.sendTitle(player, 
										5, (5 * 20), 20, 
										ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Activation ready...",
										ChatColor.WHITE + "" + ChatColor.BOLD + "Shift + Left click nexus to active.");
					    	})
					    	.delay(time_one)
					    	.sync(() -> {
					    		if (!leveldata.getCanUseItem()){
					    			leveldata.hologram().stopTask();
					    			leveldata.canUseItem(true);
					    			putItemBack(player, 0);
					    			Land nexusstructure = Land.getLand(kingdom.getNexus());
					    			nexusstructure.getStructures().get(nexusstructure.getKingdom().getNexus()).spawnHolograms(kingdom);
					    			return;
					    		}
					    	}).execute(() -> {
								ActionBar.clearActionBar(player);
					    		Titles.clearTitle(player);
								Titles.sendTitle(player, 
										5, (5 * 20), 20, 
										ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Activation ready...",
										ChatColor.WHITE + "" + ChatColor.BOLD + "Shift + Left click nexus to active.");
					    	});
						}else{
							LevelsKingdoms.newChain()
					    	.sync(() -> {
								Titles.clearTitle(player);
								Titles.sendTitle(player, 
										5, (3 * 20), 20, 
										ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "ERROR!",
										ChatColor.WHITE + "" + ChatColor.BOLD + "This nexus is waiting for a upgrade!");
					    	})
					    	.delay(4 * 20)
					    	.sync(() -> {
								Titles.clearTitle(player);
								Titles.sendTitle(player, 
										5, (5 * 20), 20, 
										ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Activation ready...",
										ChatColor.WHITE + "" + ChatColor.BOLD + "Shift + Left click nexus to active.");
					    	})
					    	.delay(time_one)
					    	.sync(() -> {
					    		if (!leveldata.getCanUseItem()){
					    			leveldata.hologram().stopTask();
					    			leveldata.canUseItem(true);
					    			putItemBack(player, 1);
					    			Land nexusstructure = Land.getLand(kingdom.getNexus());
					    			nexusstructure.getStructures().get(nexusstructure.getKingdom().getNexus()).spawnHolograms(kingdom);
					    			return;
					    		}
					    	}).execute();
						}
						
					}
			}
		}
		
	}
	
	@EventHandler
	public void UseLevelsHigher(PlayerInteractEvent event){
		Player player = event.getPlayer();
    	KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer(event.getPlayer().getUniqueId());
		if (!kingdomPlayer.hasKingdom()) return;
		Kingdom kingdominfo = kingdomPlayer.getKingdom();
		
		if (player.getGameMode() == GameMode.CREATIVE) return;
		if (kingdominfo.getNexus() == null) return;
		if (kingdomPlayer.isAdmin() && GameManagement.getadminManager().isUserEditing(player)) return;
		
		if (!GameManagement.getKingdomManager().getLoadKingdomsData().hasKingdomData(kingdominfo.getId())) return;
		Land land = Land.getLand(player.getLocation());
		if (kingdominfo != null && land.isClaimed() && land.getKingdom().getName().equalsIgnoreCase(kingdominfo.getName())) {		
			LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdominfo.getId());
			if (leveldata.getlevel() < 1) return;
			int time_more = 3 * (60 * 20);
			if (event.getAction() == Action.RIGHT_CLICK_AIR){
				if (levelsloader.getConfig(leveldata.getlevel() + 1).CheckItem(player.getInventory().getItemInMainHand())){
					IntStream.rangeClosed(0, MaxLevelFiles)
					.map(File -> File + 1)
					.limit(MaxLevelFiles)
					.forEach((Files) -> {
						if(leveldata.getlevel() == Files){
							if (kingdominfo.getResourcePoints() < levelsloader.getConfig((Files+1)).getLevelRpCost()){	
								Titles.clearTitle(player);
								Titles.sendTitle(player, 
										5, (3 * 20), 20, 
										ChatColor.DARK_RED + "" + ChatColor.BOLD + "Error low ResourcePoints!",
										ChatColor.WHITE + "" + ChatColor.BOLD + "Your Kingdom need " + levelsloader.getConfig(Files+1).getLevelRpCost() +" ResourcePoints");
		    				return;
							}else{
								startlevel(kingdominfo, player, time_more, leveldata.getlevel(), levelsloader.getConfig(leveldata.getlevel() + 1).getIcon());
							}
						}
					});
				}
			}
		}
	}
		
	@EventHandler
	public void playerCancelUpgrade(PlayerInteractEvent event) {
		@NotNull Action action = event.getAction();
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if (block == null) return;
		
		KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer(player.getUniqueId());
		Land land = Land.getLand(block.getLocation());
		Kingdom kingdom = kingdomPlayer.getKingdom();
		Structure structure = land.getStructures().get(kingdom.getNexus());
		
		
		if (action == Action.RIGHT_CLICK_BLOCK && player.isSneaking()){
			if (land != null && land.isClaimed() && structure.getStyle().getType().isNexus()){
					if (kingdom != null && land.isClaimed() && land.getKingdom().getName().equals(kingdom.getName())) {
						LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
						if (player.getGameMode() == GameMode.CREATIVE) return;
							if (kingdom != null && getUpgradeMode().containsKey(kingdom.getId())){
								if (getUpgradeMode().containsValue(player)){
										removeUpgradeMode(kingdom.getId());
										if (Methods.hasAvaliableSlot(player)){
								    		if (!leveldata.getCanUseItem()){
								    			ActionBar.sendActionBar(plugin, player, ChatColor.DARK_RED + "Cancelled upgrade mode!", 25);
								    			LevelsKingdoms.newChain()
								    			.sync(() -> {
									    			structure.spawnHolograms(kingdom);
												}).execute();
												leveldata.hologram().stopTask();
												leveldata.canUseItem(true);
								    			GameManagement.getLevelUpItemManager().putItemBack(player, 0);
								    			leveldata.CanUgradeNextLevel(true);
								    		}
										}
									
								}
							}
					}
			}
		}
	}
	
	@EventHandler
	public void onNexusClick(PlayerInteractEvent event) {
		@NotNull Action action = event.getAction();
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if (block == null) return;
		
		KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer(player.getUniqueId());
		Land land = Land.getLand(block.getLocation());
		Kingdom kingdom = kingdomPlayer.getKingdom();
		Structure structure = land.getStructures().get(kingdom.getNexus());

		if (action == Action.LEFT_CLICK_BLOCK && player.isSneaking()){
			if (land != null && land.isClaimed() && structure.getStyle().getType().isNexus()){
					if (kingdom != null && land.getKingdom().getName().equals(kingdom.getName())) {
						LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
						if (player.getGameMode() == GameMode.CREATIVE) return;
						if (!GameManagement.getKingdomManager().getLoadKingdomsData().hasKingdomData(kingdom.getId())) return;
						if (GameManagement.getLevelUpManager().getLevel(kingdom) != 0) return;
						if (leveldata.getUgradeNextLevel()){
								if (leveldata.getCanUseItem()){
	            					leveldata.CanUgradeNextLevel(false);
	            					setUpgradeMode(kingdom.getId(), player);
									Bukkit.getScheduler().runTaskLater(LevelsKingdoms.getInstance(), ()->{
										structure.removeHolograms();
							    		startUpgradeMode(kingdom, player);
									}, 1 * 20);
                				}else{
									Titles.clearTitle(player);
									Titles.sendTitle(player, 
											5, (3 * 20), 20, 
											ChatColor.DARK_RED + "" + ChatColor.BOLD + "ERROR!",
											ChatColor.WHITE + "" + ChatColor.BOLD + "Level 1 Artifect not activeted!");
									return;
                				}
						}else{
							Titles.clearTitle(player);
							Titles.sendTitle(player, 
									5, (3 * 20), 20, 
									ChatColor.DARK_RED + "" + ChatColor.BOLD + "ERROR!",
									ChatColor.WHITE + "" + ChatColor.BOLD + "This nexus is already running in upgrade mode!");
							Bukkit.getScheduler().runTaskLater(LevelsKingdoms.getInstance(), ()->{
								GameManagement.getLevelUpManager().getUpgradeMsg(player);
							}, 4 * 20);
        				return;
						}
					}
				}
			}
		
		
	}
	
	
	public void startlevel(Kingdom kingdominfo, Player player, int time, int level, XMaterial item){

		LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdominfo.getId());
		
		if (leveldata.getCanUseItem()){
			LevelsKingdoms.newChain()
	    	.sync(() -> {
				if (player.getInventory().getItemInMainHand().getAmount() > 1){
					player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);		
				}else{
					player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
				}
				SimpleLocation nexus = kingdominfo.getNexus();
				leveldata.canUseItem(false);
				leveldata.hologram().startRotation(nexus.clone().toBukkitLocation().add(0.5, 1.5, 0.5), placeHologram(nexus.clone().toBukkitLocation().add(0.5, 1.5, 0.5), item), time);
				Titles.clearTitle(player);
				Titles.sendTitle(player, 
						5, (5 * 20), 20, 
						ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Activation ready...",
						ChatColor.WHITE + "" + ChatColor.BOLD + "Open the level nexus menu to upgrade.");
	    	})
	    	.delay(time)
	    	.sync(() -> {
	    		if (!leveldata.getCanUseItem()){
					leveldata.hologram().stopTask();
					leveldata.canUseItem(true);
	    			putItemBack(player, level);
	    		}
	    	}).execute();
		}else{
			LevelsKingdoms.newChain()
	    	.sync(() -> {
				Titles.clearTitle(player);
				Titles.sendTitle(player, 
						5, (3 * 20), 20, 
						ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "ERROR!",
						ChatColor.WHITE + "" + ChatColor.BOLD + "This nexus is waiting for a upgrade!");
	    	})
	    	.delay(4 * 20)
	    	.sync(() -> {
				Titles.clearTitle(player);
				Titles.sendTitle(player, 
						5, (5 * 20), 20, 
						ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Activation ready...",
						ChatColor.WHITE + "" + ChatColor.BOLD + "Open the level nexus menu to upgrade.");
	    	})
	    	.delay(time)
	    	.async(() -> {
	    		if (!leveldata.getCanUseItem()){
					leveldata.hologram().stopTask();
					leveldata.canUseItem(true);
	    			putItemBack(player, level);
	    		}
	    	}).execute();
		}
	}
	
	public void putItemBack(Player player, int level){
		ItemStack item = levelsloader.getConfig(level).getFinalItem();
		XItemStack.giveOrDrop(player, item);
		Titles.clearTitle(player);
		Titles.sendTitle(player, 
			5, (3 * 20), 20, 
			ChatColor.DARK_RED + "" + ChatColor.BOLD + "Warning...",
			ChatColor.WHITE + "" + ChatColor.BOLD + "Putting item back or on the Ground!");
	}
	
	public Hologram placeHologram(Location loc, XMaterial item){		
		ItemStack itemStack1 = item.parseItem();
		Hologram hologram = HologramsAPI.createHologram(LevelsKingdoms.getInstance(), loc);
		hologram.appendItemLine(itemStack1);
		VisibilityManager visiblityManager = hologram.getVisibilityManager();
		visiblityManager.setVisibleByDefault(true);
		visiblityManager.resetVisibilityAll();
		
		return hologram;
	}

	public void startUpgradeMode(Kingdom kingdom, Player player){

		LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());

		if (getUpgradeMode().containsKey(kingdom.getId())){
			Titles.clearTitle(player);
			Titles.sendTitle(player, 
					5, (5 * 20), 20, 
					ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Starting up upgrade!",
					ChatColor.WHITE + "" + ChatColor.BOLD + "To stop it sneak + rightclick nexus, else it will start after 10 seconds!");
				Bukkit.getScheduler().runTaskLater(plugin, () -> {
					if (getUpgradeMode().containsKey(kingdom.getId())){
						if (!leveldata.getCanUseItem()){
							leveldata.hologram().stopTask();
							leveldata.canUseItem(true);
							GameManagement.getLevelUpManager().CreateLevelup(player, kingdom);
						}
					}
				}, 10 * 20);
		}
	}
	
	@Override
	public void onDisable() {

	}

}
