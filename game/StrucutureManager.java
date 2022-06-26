package me.nostyll.Kingdoms.levels.game;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.kingdoms.gui.InteractiveGUI;

import me.nostyll.Kingdoms.levels.handler.manager.Manager;

public class StrucutureManager extends Manager implements Listener{
	
	InteractiveGUI levelsgui;
	
	protected StrucutureManager(Plugin plugin) {
		super(plugin);
		this.registerStructures();
		this.registerTurrets();
	}
	
	public void registerStructures(){
		
	}
	
	public void registerTurrets(){

	}
	

	@Override
	public void onDisable() {
		
	}

}
