package me.nostyll.Kingdoms.levels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kingdoms.constants.group.Kingdom;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import me.nostyll.Kingdoms.levels.commands.CommandManager;
import me.nostyll.Kingdoms.levels.configuration.LevelConfig;
import me.nostyll.Kingdoms.levels.configuration.LevelsLang;
import me.nostyll.Kingdoms.levels.game.GameManagement;
import me.nostyll.Kingdoms.levels.handler.levels.LevelsConfigLoader;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;
import me.nostyll.Kingdoms.levels.utils.Chat;
import me.spomg.plugins.api.minecord.MAPI;

public class LevelsKingdoms extends JavaPlugin implements Listener {

	private boolean useHolographicDisplays;
	public static LevelConfig config;
	public static LevelsConfigLoader Levelsconfig;

	private static CommandManager commandManager;

	private static LevelsKingdoms instance;

	private static GameManagement managers;

	private static TaskChainFactory taskChainFactory;

	private static int max_levels = 12;


	@Override
	public void onEnable() {
		instance = this;
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		PluginManager pm = Bukkit.getPluginManager();

		console.sendMessage(Chat.formatText("&a============================="));
		console.sendMessage(Chat.formatText(
				String.format("&7%s %s by &5Nostyll <3&7!", this.getName(), this.getDescription().getVersion())));

		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
			getLogger().severe("*** This plugin will be disabled. ***");
			this.setEnabled(false);
			return;
		}

		setUseHolographicDisplays(pm.isPluginEnabled("HolographicDisplays"));

		// TaskManagaer
		console.sendMessage(Chat.formatText("&7Action: &aLoading the TaskManager&7..."));
		taskChainFactory = BukkitTaskChainFactory.create(this);

		taskChainFactory.setDefaultErrorHandler((e, t) -> {
			LevelsKingdoms.logInfo("Task " + t.hashCode() + " generated an exception:");
			e.printStackTrace();
		});

		config = new LevelConfig(this);
		Levelsconfig = LevelsConfigLoader.getLevelsLoader();
		LevelsLang.reload();

		managers = new GameManagement(this);

		try {
			commandManager =  new CommandManager(this);
        } catch (Exception e) {
			LevelsKingdoms.logDebug("Exception : Failed to initialize command manager" +  e);
            this.setEnabled(false);
            return;
        }

		for (Manager manager : Manager.getModules()) {
			if (manager == null) {
				LevelsKingdoms.logDebug("Null manager found. skipped.");
				continue;
			}
			try {
				pm.registerEvents(manager, instance);
				LevelsKingdoms.logInfo(manager.getClass().getSimpleName() + " loaded");
			} catch (Exception e) {
				LevelsKingdoms.logDebug("Exception : " + manager.getClass().getSimpleName());
			} catch (Error e) {
				LevelsKingdoms.logDebug("Error : " + manager.getClass().getSimpleName());
			}
		}

		///Load Minecord because it will load differnt
		try{
			LevelsKingdoms.logInfo("MinecordAPI Hooked!");
			LevelsKingdoms.logInfo("MinecordAPI Version: " + pm.getPlugin("Minecord").getDescription().getVersion());
			LevelsKingdoms.logInfo("MinecordAPI starting Kingdoms to discord connection...");

		}catch(UnsupportedOperationException event){
			LevelsKingdoms.logInfo("MinecordAPI: " + event.getMessage());
		}

		console.sendMessage(Chat.formatText("&7Action: Setting up commands"));

		console.sendMessage(Chat.formatText("&7Action: &aEnabling&7..."));
		console.sendMessage(Chat.formatText("&a============================="));
	}

	public boolean isUseHolographicDisplays() {
		return useHolographicDisplays;
	}

	public void setUseHolographicDisplays(boolean useHolographicDisplays) {
		this.useHolographicDisplays = useHolographicDisplays;
	}
	
	public void reload(){
		reloadConfig();
		config.reload();
		config = new LevelConfig(this);

	}
	
	public static LevelsKingdoms getInstance() {
		return instance;
	}
    
	public static GameManagement getManagers() {
		return managers;
	}
	
	public static LevelsConfigLoader getLevelsConfigLoader(){
		return Levelsconfig;
	}
	
	public static CommandManager getcommandManager(){
		return commandManager;
	}
	public static void logDebug(String str){
		if(!config.isDebugging)
			return;

		Bukkit.getLogger().warning(instance.getName()+"(Debug) "+str);
	}
	
	public static void logInfo(String str){
		Bukkit.getLogger().info("[" + instance.getName()+"] "+ChatColor.translateAlternateColorCodes('&', str));
	}

	public static void logColor(String str){
		if(!config.isMonitoring)
			return;
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + instance.getName() + "]: " + str);
	}

	public static int getMax(){
		return max_levels;
	}
	
    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }
    public static <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }


	@Override
	public void onDisable() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(Chat.formatText("&a============================="));
        console.sendMessage(Chat.formatText("&7Kingdoms Level " + this.getDescription().getVersion() + " by &55Nostyll <3!"));
        
		if (!getLevelsConfigLoader().levels().isEmpty()){
			getLevelsConfigLoader().levels().values().forEach(save -> save.save());
		}

        GameManagement.getKingdomManager().getLoadKingdomsData().getAllKingdomdata().forEach((k, v) -> {
        	Kingdom kingdominfo = Kingdom.getKingdom(k);
			if (v.getlevel() > 0){
				LevelsKingdoms.logInfo("Removing all HoloGrams and Level Nexus");
				GameManagement.getLevelUpManager().removeHoloAndLevel(kingdominfo);
			}
		});

		commandManager = null;

		if (config.isDebugging){
			Plugin debug = Bukkit.getPluginManager().getPlugin("Kingdoms");
			Bukkit.getPluginManager().disablePlugin(debug);
		}
        
        console.sendMessage(Chat.formatText("&7Action: &cDisabling&7..."));
        console.sendMessage(Chat.formatText("&a============================="));

	}

}
