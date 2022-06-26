package me.nostyll.Kingdoms.levels.handler.levels;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.IntStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.handler.items.ItemLoader;

public class LevelsConfigLoader {
	
	public HashMap<Integer, ItemLoader> ConfigItems = new HashMap<Integer, ItemLoader>();
	Plugin plugin;
	
	public LevelsConfigLoader(Plugin plugin){
		this.plugin = plugin;
		loadLevelsFile();
	}

	public void loadLevelsFile(){
		
		IntStream.rangeClosed(0, LevelsKingdoms.getMax())
		.map(File -> File + 1)
		.limit(LevelsKingdoms.getMax())
		.forEach((Files) -> {
				File directory = new File(LevelsKingdoms.getInstance().getDataFolder() + "/levels/");
				if (! directory.exists()){
					directory.mkdir();
				}
			    File saveFile = new File(LevelsKingdoms.getInstance().getDataFolder() + "/levels/", "level" + Files + ".yml");
				if(!saveFile.exists())
				try {
					saveFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				FileConfiguration config = YamlConfiguration.loadConfiguration(saveFile);
			    levels().put(Files, new ItemLoader(config, saveFile));
				}
			);		
	}
	
	public HashMap<Integer, ItemLoader> levels(){
		return this.ConfigItems;
	}
	
	public ItemLoader getConfig(int level){
		return ConfigItems.get(level);
	}
	
	
	public static LevelsConfigLoader getLevelsLoader(){
		return new LevelsConfigLoader(LevelsKingdoms.getInstance());
	}
	
}
