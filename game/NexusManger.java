package me.nostyll.Kingdoms.levels.game;

import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import org.jetbrains.annotations.NotNull;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.land.Land;
import org.kingdoms.constants.land.structures.Structure;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.events.lands.NexusMoveEvent;
import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;
import me.nostyll.Kingdoms.levels.utils.Methods;

public class NexusManger extends Manager implements Listener{
	
	public final LevelsKingdoms plugin;

	
	TextLine textline;
	
	protected NexusManger(LevelsKingdoms plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void moveNexus(NexusMoveEvent event){
		if (event.getFrom() == null) return;
		Block newnexus = event.getTo().getBlock();
		Player player = event.getKingdomPlayer().getPlayer();
		Kingdom kingdom = event.getKingdomPlayer().getKingdom();
		KingdomPlayer kp = event.getKingdomPlayer();
		if (GameManagement.getLevelUpManager().getLevel(kingdom) == 0) return;
		
    	LevelsKingdoms.newChain()
    	.sync(() -> {
    		sentMoveMessage(player, true);
    		GameManagement.getLevelUpManager().removeHoloAndLevel(kingdom);
    	})
    	.delay(2, TimeUnit.SECONDS)
    	.sync(() -> {
    		sentMoveMessage(player, false);
    		GameManagement.getLevelUpManager().setNexuslevelfirst(kingdom, kp, newnexus.getLocation());
    	}).execute();
	}
	
	public void sentMoveMessage(Player player, boolean first){
		if (first){
			Titles.clearTitle(player);
			Titles.sendTitle(player, 
					5, (3 * 20), 20, 
					ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Moving...",
					ChatColor.YELLOW + "" + ChatColor.BOLD + "Trying to move your nexus..");
		}else{
			Titles.clearTitle(player);
			Titles.sendTitle(player, 
					5, (3 * 20), 20, 
					ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Moving...Done",
					ChatColor.WHITE + "" + ChatColor.BOLD + "We have moved your nexus.");
		}
	}

	
	@Override
	public void onDisable() {
		
	}

}
