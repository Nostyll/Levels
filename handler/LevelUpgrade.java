package me.nostyll.Kingdoms.levels.handler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.kingdoms.constants.group.Kingdom;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.events.UpdateUpgradesEvent;
import me.nostyll.Kingdoms.levels.utils.MetadataKingdom;

public class LevelUpgrade {
	
	LevelsKingdoms plugin = LevelsKingdoms.getInstance();
	
	private final Map<String, Boolean> enabled = new HashMap<>();
	private final Map<String, Boolean> bought = new HashMap<>();
	private final LoadKingdoms loadkingdom;
	private final Kingdom kingdom;
	
	public LevelUpgrade(Kingdom kingdom, LoadKingdoms loadKingdoms) {
		this.kingdom = kingdom;
		this.loadkingdom = loadKingdoms;
		checkEnabled();
		checkBought();
	}
	
	public LoadKingdoms getLoadKingdom() {
		return loadkingdom;
	}
	
	public Kingdom getKingdom() {
		return kingdom;
	}
	
	public void checkEnabled(){
		this.setEnabled("discord", true);
	}
	
	public void checkBought(){
		MetadataKingdom upgradeMetadata = MetadataKingdom.getMetadata(this.loadkingdom.getMetaValue("upgrades").getString());
		setMinecord(upgradeMetadata.getBoolean("discord"));
	}

	public boolean hasBought(String upgrade) {
		if (upgrade == null)
			return false;
		return bought.getOrDefault(upgrade, false);
	}
	
	public boolean hasLevelUpgrade(){
		if (bought.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	public void setBought(String upgrade, boolean bought) {
		this.bought.put(upgrade, bought);
		getLoadKingdom().Upgradesmetadata.put(upgrade, bought);
		
		Bukkit.getScheduler().runTaskAsynchronously(LevelsKingdoms.getInstance(), () -> {
    		LevelsKingdoms.logInfo("&7Action: &aCall LoadlevelEvent now!");
    		Bukkit.getPluginManager().callEvent(new UpdateUpgradesEvent(loadkingdom, kingdom, upgrade));
		});
	}
	
	public boolean isEnabled(String upgrade) {
		if (upgrade == null)
			return false;
		if (!hasBought(upgrade)){
			return false;
		}
		return enabled.getOrDefault(upgrade, false);
	}

	public void setEnabled(String upgrade, boolean enabled) {
		this.enabled.put(upgrade, enabled);
	}

	/**
	 * This part is to enable Minecord
	 */
	public boolean hasMinecord() {
		return isEnabled("minecord");
	}

	public void setMinecord(boolean Minecord) {
		setBought("minecord", Minecord);
	}

}
