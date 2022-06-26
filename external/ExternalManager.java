package me.nostyll.Kingdoms.levels.external;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;

public class ExternalManager<plugin> extends Manager {

	//private static NameTagManager nametagManager;
	//private static GuardianBeamManager guardianBeamManager;
	private static MinecordManager minecordManager;
	private static DiscordSRVManager discordSRVManager;
	
	public ExternalManager(Plugin plugin) {
		super(plugin);
		checkSoftDepends(plugin);
	}
	
	private void checkSoftDepends(Plugin plugin){
		PluginManager pm = Bukkit.getPluginManager();

		
		if (minecordManager == null && pm.getPlugin("Minecord") != null){
			minecordManager = new MinecordManager(plugin);
		}
		
		if (discordSRVManager == null && pm.getPlugin("DiscordSRV") != null){
			discordSRVManager = new DiscordSRVManager(plugin);
		}
		
	}
	
	public static MinecordManager getMinecordManager() {
		return minecordManager;
	}
	
	public static DiscordSRVManager getDiscordSRVManager() {
		return discordSRVManager;
	}	

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event){
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {checkSoftDepends(plugin);});
	}
	
	@Override
	public void onDisable() {

	}



}
