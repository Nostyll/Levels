package me.nostyll.Kingdoms.levels.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.kingdoms.constants.group.Kingdom;

import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;

public class UpdateUpgradesEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	
	private Kingdom kingdom;
	private LoadKingdoms leveldata;
	private String upgrade;
	
	public UpdateUpgradesEvent(LoadKingdoms level, Kingdom kingdom, String upgrade){
		super(true);
		this.kingdom = kingdom;
		this.leveldata = level;
		this.upgrade = upgrade;
	}
	
	public Kingdom getKingdom(){
		return this.kingdom;
	}
	
	public LoadKingdoms getLevelData(){
		return leveldata;
	}
	
	public String getUpgrade(){
		return this.upgrade;
	}
	
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
