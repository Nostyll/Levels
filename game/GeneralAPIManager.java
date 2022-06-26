package me.nostyll.Kingdoms.levels.game;

import org.bukkit.plugin.Plugin;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;


/**
 * collection of generally used APIs
 * @author wysohn
 *
 */
public class GeneralAPIManager extends Manager {
	
	private boolean holoAPIEnabled = false;
	private boolean protocolLibEnabled = false;
	private boolean headdatabaseEnabled = false;


	protected GeneralAPIManager(Plugin plugin) {
		super(plugin);

			try{
				Class.forName("com.gmail.filoghost.holographicdisplays.HolographicDisplays");
				Plugin holo = plugin.getServer().getPluginManager().getPlugin("HolographicDisplays");
				if(holo != null){
					LevelsKingdoms.logInfo("HolographicDisplays Hooked!");
					LevelsKingdoms.logInfo("Version: " + ((Plugin) holo).getDescription().getVersion());
					if(((Plugin)holo).isEnabled()){
						holoAPIEnabled = true;
					}else{
						LevelsKingdoms.logInfo("HolographicDisplays is not enabled!");
						LevelsKingdoms.logInfo("HolographicDisplays is not found, im going to disable myself!");
						LevelsKingdoms.getInstance().getServer().getPluginManager().disablePlugin(plugin);						
					}
				}
			} catch (NoClassDefFoundError e) {
				LevelsKingdoms.logInfo("HolographicDisplays: NoClassDefFoundError");
			} catch (Exception e){
				LevelsKingdoms.logInfo("HolographicDisplays: Exception");
			}
			
			try{
				Class.forName("me.arcaniax.hdb.Main");
				Plugin headdatabase = plugin.getServer().getPluginManager().getPlugin("HeadDatabase");
				if(headdatabase != null){
					LevelsKingdoms.logInfo("HeadDatabase Hooked!");
					LevelsKingdoms.logInfo("Version: " + ((Plugin) headdatabase).getDescription().getVersion());
					if(((Plugin)headdatabase).isEnabled()){
						headdatabaseEnabled = true;
					}else{
						LevelsKingdoms.logInfo("HeadDatabase is not enabled!");
						LevelsKingdoms.logInfo("HeadDatabase is not found, im going to disable myself!");
						LevelsKingdoms.getInstance().getServer().getPluginManager().disablePlugin(plugin);					
					}
				}
			} catch (NoClassDefFoundError e) {
				LevelsKingdoms.logInfo("HolographicDisplays: NoClassDefFoundError");
			} catch (Exception e){
				LevelsKingdoms.logInfo("HolographicDisplays: Exception");
			}
		
		try{
			Plugin protocolLib = plugin.getServer().getPluginManager().getPlugin("ProtocolLib");
			if(protocolLib != null){
				LevelsKingdoms.logInfo("protocolLib Hooked!");
				LevelsKingdoms.logInfo("Version: " + ((Plugin) protocolLib).getDescription().getVersion());
				if(((Plugin)protocolLib).isEnabled()){
					protocolLibEnabled = true;
				}else{
					LevelsKingdoms.logInfo("protocolLib is not enabled!");
					LevelsKingdoms.logInfo("Disabled support for protocolLib.");						
				}
			}
		} catch (NoClassDefFoundError e) {
			LevelsKingdoms.logInfo("protocolLib: NoClassDefFoundError");
		} catch (Exception e){
			LevelsKingdoms.logInfo("protocolLib: Exception");
		}
	
	}


	public boolean isHoloAPIEnabled() {
		return holoAPIEnabled;
	}
	
	public boolean isHeaddatabaseEnabled(){
		return headdatabaseEnabled;
	}
	
	public HeadDatabaseAPI getHeaddatabase(){
		HeadDatabaseAPI api = null;
		
		if (isHeaddatabaseEnabled()){
			api = new HeadDatabaseAPI();
		}
		
		return api;
	}

	public boolean isProtocolLibEnabled() {
		return protocolLibEnabled;
	}

	@Override
	public void onDisable() {
	}

}
