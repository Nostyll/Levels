package me.nostyll.Kingdoms.levels.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.events.members.KingdomJoinEvent;
import org.kingdoms.events.members.KingdomLeaveEvent;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.events.LoadLevelEvent;
import me.nostyll.Kingdoms.levels.events.UpdateUpgradesEvent;
import me.nostyll.Kingdoms.levels.external.ExternalManager;
import me.nostyll.Kingdoms.levels.game.GameManagement;
import me.nostyll.Kingdoms.levels.handler.DiscordKingdoms;
import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;
import me.spomg.plugins.api.minecord.MAPI;

public class KingdomDiscordManager extends Manager implements Listener{

	public KingdomDiscordManager(Plugin plugin) {
		super(plugin);
		LevelsKingdoms.logInfo("MinecordAPI started Kingdoms to discord connection!");
	}
	
	@EventHandler
	public void checkForUpgrades(UpdateUpgradesEvent event){
		
	}
	
	@EventHandler
	public void AddPlayerToMembersDiscord(PlayerJoinEvent event){
		
		LevelsKingdoms.logDebug("Kingdom Player login the server!");
		Player player = event.getPlayer();
		
		KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player.getUniqueId());
		Kingdom kingdom = kp.getKingdom();
		if (kingdom != null)return;
		
		
		DiscordKingdoms discordkingdom = this.getLevelsData(kingdom).getdiscordKingdoms();
		discordkingdom.createMemberlist(kp); 	
	}
	
	@EventHandler
	public void loadDiscordOnLevelup(LoadLevelEvent event){
    		if (!event.getLevelupevent()) return;
			Kingdom kingdom = event.getKingdom();
			LoadKingdoms leveldata = event.getLevelData();
			if (CheckKingdomLevel(kingdom, leveldata)){
				DiscordKingdoms discordkingdom = this.getLevelsData(event.getKingdom()).getdiscordKingdoms();
				if (leveldata.getLevelUpgrades().hasMinecord() && discordkingdom.getPlayer().size() >= 2){
					discordkingdom.createVoiceChannel(kingdom);
					LevelsKingdoms.logInfo("[LoadLevelEvent] We are level up and we have Minecord, so build the channel");
				}
			}
	}
	
	@EventHandler
	public void memberJoinKingdom(KingdomJoinEvent event){

		MAPI minecord = LevelsKingdoms.getInstance().getServer().getServicesManager().load(MAPI.class);
    		if (event.getPlayer() == null) return;
    		KingdomPlayer p = event.getPlayer();
    		Player player = p.getPlayer();
    		
    		if (this.getLevelsData(event.getKingdom()) == null && this.getLevelsData(event.getKingdom()).getdiscordKingdoms() == null) return;
    		
    		DiscordKingdoms discordkingdom = this.getLevelsData(event.getKingdom()).getdiscordKingdoms();
    		
    		
    		if (!discordkingdom.hasPlayer(player) && minecord.isPlayerLinked(player)){
    			discordkingdom.addPlayer(player);
    		}
	}
	
	@EventHandler
	public void removeDiscordChannel(KingdomLeaveEvent event){
		KingdomPlayer kingdomPlayer = event.getKingdomPlayer();
		if (kingdomPlayer.getKingdom() == null) return;
		Kingdom kingdom = kingdomPlayer.getKingdom();
		DiscordKingdoms discordkingdom = this.getLevelsData(kingdom).getdiscordKingdoms();
		Bukkit.getScheduler().runTaskLaterAsynchronously(LevelsKingdoms.getInstance(), () -> {
			LevelsKingdoms.logDebug("Kingdom Player logoff the server!");
    		if (discordkingdom.getPlayer().size() < 2 && discordkingdom.getVoiceChannel() != null){
    			discordkingdom.removeVoiceChannel();
    		}
		} , 3 * 20);
	}
	
	public boolean CheckKingdomLevel(Kingdom kingdom ,LoadKingdoms leveldata){
		
		boolean isloaded = GameManagement.getKingdomManager().getLoadKingdomsData().hasKingdomData(kingdom.getId());
		if (isloaded && leveldata.getlevel() >= LevelsKingdoms.config.minecord){
			return true;
		}
		return false;
		
	}
	
	public LoadKingdoms getLevelsData(Kingdom kingdom){
		if (GameManagement.getKingdomManager().getLoadKingdomsData().hasKingdomData(kingdom.getId())){
			LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
			return leveldata;
		}
		return null;
	}
	
	@Override
	public void onDisable() {
		
	}
}
