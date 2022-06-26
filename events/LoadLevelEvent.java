package me.nostyll.Kingdoms.levels.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.kingdoms.constants.group.Kingdom;

import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;

public class LoadLevelEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	
	private Kingdom kingdom;
	private LoadKingdoms leveldata;
	private boolean cancelled;
	private boolean levelupevent = false;
	
	
	public LoadLevelEvent(LoadKingdoms level, Kingdom kingdom){
		super(true);
		this.kingdom = kingdom;
		this.leveldata = level;
	}
	
	public LoadLevelEvent(LoadKingdoms level, Kingdom kingdom, boolean levelupevent){
		super(true);
		this.kingdom = kingdom;
		this.leveldata = level;
		this.levelupevent = levelupevent;
	}
	
	public int getLevel(){
		return this.leveldata.getlevel();
	}
	
	public Kingdom getKingdom(){
		return this.kingdom;
	}
	
	public LoadKingdoms getLevelData(){
		return leveldata;
	}
	
	public void setLevelupevent(boolean levelupevent){
		this.levelupevent = levelupevent;
	}
	
	public boolean getLevelupevent(){
		return levelupevent;
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
