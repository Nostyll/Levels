package me.nostyll.Kingdoms.levels.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public final class LevelConfig {
	private FileConfiguration config;
	private File saveFile;
	
	public boolean isDebugging = false;
	public boolean isMonitoring = false;
	
	public int maxLevels = 6;
	public int minecord = 2;

	public String MAIN_COMMAND_LABEL = "levelskingdoms";
    public List<String> MAIN_COMMAND_ALIASES = new ArrayList<>();
	
	public LevelConfig(Plugin plugin){

		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdirs();
		
		saveFile = new File(plugin.getDataFolder(), "config.yml");
		
		if(!saveFile.exists())
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		config = YamlConfiguration.loadConfiguration(saveFile);
		
		load();
		save();
	}
	
	public void load(){
		this.isDebugging = config.getBoolean("Log.isDebugging");
		this.isMonitoring = config.getBoolean("Log.isMonitoring");
		this.maxLevels = config.getInt("maxLevels");
		this.minecord = config.getInt("Minecord.isactivelevel");
		MAIN_COMMAND_LABEL = config.getString("settings.commands.main-command-label", MAIN_COMMAND_LABEL);
        MAIN_COMMAND_ALIASES.clear();
        config.getList("settings.commands.main-command-aliases", List.of(
                "map"
        )).forEach(entry -> MAIN_COMMAND_ALIASES.add(entry.toString()));
	}
	
	public void save(){
		config.set("Log.isDebugging", this.isDebugging);
		config.set("Log.isMonitoring", this.isMonitoring);
		
		config.set("maxLevels", this.maxLevels);
		config.set("Minecord.level", this.minecord);

		config.set("settings.commands.main-command-label", MAIN_COMMAND_LABEL);
		config.set("settings.commands.main-command-aliases", MAIN_COMMAND_ALIASES);
		
		try {
			config.save(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reload(){
		try {
			config.load(saveFile);
			config.save(saveFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
