package me.nostyll.Kingdoms.levels.game;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.data.DataHandler;
import org.kingdoms.events.general.KingdomCreateEvent;
import org.kingdoms.events.general.KingdomDisbandEvent;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.events.LoadLevelEvent;
import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;
import me.nostyll.Kingdoms.levels.handler.LoadKingdomData;

public class KingdomManager extends Manager implements Listener{
	
	public LoadKingdomData loadKingdomData;
	
	public KingdomManager(Plugin plugin) {
		super(plugin);
		this.loadKingdomData = LoadKingdomData.getLoadKingdomData();
		LoadKingdoms();
	}

	public LoadKingdomData getLoadKingdomsData(){
		return loadKingdomData;

	}

	public void LoadKingdoms(){
		Bukkit.getScheduler().runTaskAsynchronously(LevelsKingdoms.getInstance(), ()-> {
			DataHandler.get().getKingdomManager().getKingdoms().forEach(kingdom -> {
				if (!getLoadKingdomsData().hasKingdomData(kingdom.getId())){
					getLoadKingdomsData().createKingdomData(kingdom.getId());
					LoadKingdoms kingdomsdata = getLoadKingdomsData().getKingdomData(kingdom.getId());
					kingdomsdata.getKingdomInfo();
					kingdomsdata.getLevelUpgrades();
					LevelsKingdoms.logDebug("&7Action: &aCall LoadlevelEvent now! + " + kingdom.getName());
					Bukkit.getPluginManager().callEvent(new LoadLevelEvent(getLoadKingdomsData().getKingdomData(kingdom.getId()), kingdom));
				}
			});
		});
	}

	@EventHandler
	public void LoadKingdom(PlayerJoinEvent event){
		Player player = event.getPlayer();

		KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);

		if (kp.getKingdom() != null){
			Kingdom kingdom = kp.getKingdom();
			if (!getLoadKingdomsData().hasKingdomData(kingdom.getId())){
				getLoadKingdomsData().createKingdomData(kingdom.getId());
				LoadKingdoms kingdomsdata = getLoadKingdomsData().getKingdomData(kingdom.getId());
				kingdomsdata.getKingdomInfo();
				kingdomsdata.getLevelUpgrades();
				Bukkit.getScheduler().runTaskAsynchronously(LevelsKingdoms.getInstance(), ()-> {
					LevelsKingdoms.logDebug("&7Action: &aCall LoadlevelEvent now! + " + kingdom.getName());
					Bukkit.getPluginManager().callEvent(new LoadLevelEvent(getLoadKingdomsData().getKingdomData(kingdom.getId()), kingdom));
				} );
			}
		}


	}

	@EventHandler
	public void startCreatingKingdom(KingdomCreateEvent event){
		Kingdom kingdom = event.getKingdom();
    	LevelsKingdoms.logDebug("&7Action: &aTrying to load Kingdom&7...");
    	if (!getLoadKingdomsData().hasKingdomData(kingdom.getId())){
			getLoadKingdomsData().createKingdomData(kingdom.getId());
    	}

		Bukkit.getScheduler().runTaskLater(LevelsKingdoms.getInstance(), () -> {
			if (getLoadKingdomsData().hasKingdomData(kingdom.getId())){
				LoadKingdoms kingdomsdata = getLoadKingdomsData().getKingdomData(kingdom.getId());
				kingdomsdata.createKingdomInfo();
				LevelsKingdoms.logDebug("&7Action: &aTrying to add new data&7...");
				}
		}, 2 * 20);
	}
	
	@EventHandler
	public void removeKingdom(KingdomDisbandEvent event){
		Kingdom kingdom = event.getKingdom();
    		if (getLoadKingdomsData().hasKingdomData(kingdom.getId())){
    			LevelsKingdoms.logDebug("&7Action: &4Removing Kingdoms data from Kingdoms... " + kingdom.getName());
				if (GameManagement.getLevelUpManager().getLevel(kingdom) > 0 ) GameManagement.getLevelUpManager().removeHoloAndLevel(kingdom);
    			getLoadKingdomsData().removeKingdomData(kingdom.getId());
    		}
	}

	@Override
	public void onDisable() {
		getLoadKingdomsData().getAllKingdomdata().forEach((uuid, LoadKingdom) -> {
			LoadKingdom.saveMetaData();
		} );
		
		
	}
	

}
