package me.nostyll.Kingdoms.levels.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.external.ExternalManager;
import me.spomg.plugins.api.minecord.MAPI;
import me.spomg.plugins.api.minecord.utils.MVoice;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class DiscordKingdoms {
	
	List<UUID> players = new ArrayList<UUID>();
	Random random = new Random();
	
	MVoice voice = null;
	
	private final LoadKingdoms kingdom;

	MAPI minecord = LevelsKingdoms.getInstance().getServer().getServicesManager().load(MAPI.class);
	
	public DiscordKingdoms(LoadKingdoms loadKingdoms) {
		this.kingdom = loadKingdoms;
	}
	
	public LoadKingdoms getKingdom() {
		return kingdom;
	}
	
	public void createVoiceChannel(Kingdom kingdom){
		
		LevelsKingdoms.logDebug("Creating a discord voiceChat now!");
		
		Player randomPlayer = Bukkit.getPlayer(this.players.get(this.random.nextInt(this.players.size())));
		Player randomPlayer1 = Bukkit.getPlayer(this.players.get(this.random.nextInt(this.players.size())));
		
		while(randomPlayer1 == randomPlayer){
			randomPlayer1 = Bukkit.getPlayer(this.players.get(this.random.nextInt(this.players.size())));
		}
		
		Member member1 = minecord.getMemberFromLinkedPlayer(randomPlayer);
		Member member2 = minecord.getMemberFromLinkedPlayer(randomPlayer1);
		
		voice = minecord.createVoice(member1, member2);
		VoiceChannel channel = voice.getChannel();
		String name = channel.getName();
	    channel.getManager().setName("Kingdom_"+ name +": " + kingdom.getName()).queue();
	}
	
	public void createMemberlist(KingdomPlayer event){

		Player player = event.getPlayer();
		Kingdom kingdom = event.getKingdom();
			
		if (minecord.isPlayerLinked(player)){
			LevelsKingdoms.logDebug("Player: " + player.getName() + " has been added to the discord list.");
			
			if (!hasPlayer(player)){
				this.addPlayer(player);
				if (voice == null && getPlayer().size() >= 2){
					createVoiceChannel(kingdom);
				}
			}
		}
	}
	
	public MVoice getVoiceChannel(){
		return voice;
	}
	
	public void removeVoiceChannel(){
		
		if (getVoiceChannel() != null)
		minecord.deleteVoice(voice);
	}
	
	public void addMemberToVoice(Player player){
		Member member = minecord.getMemberFromLinkedPlayer(player);
		voice.connectMembers(member);
	}
	
	public void addPlayer(Player player){
		this.getPlayer().add(player.getUniqueId());
	}
	
	public void removePlayer(Player player){
		this.getPlayer().remove(player.getUniqueId());
	}
	
	public boolean hasPlayer(Player player){
		if (this.getPlayer().contains(player.getUniqueId())){
			return true;
		}else{
			return false;
		}
	}
	
	public List<UUID> getPlayer(){
		return this.players;
	}
	
	public MVoice getVoice(){
		return this.voice;
	}

}
