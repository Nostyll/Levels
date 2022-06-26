package me.nostyll.Kingdoms.levels.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.data.KingdomMetadata;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.utils.MetadataKingdom;
import me.nostyll.Kingdoms.levels.utils.RotateHolograms;

public class LoadKingdoms {

	LevelsKingdoms instance = null;
    UUID uuid = null;
    int level = 0;
    LevelUpgrade levelupgarde = null;
	DiscordKingdoms discordKingdoms = null;

	RotateHolograms rotatehologram = new RotateHolograms();
    Boolean canUseItem = true;
	Boolean CanUgradeNextLevel = true;
    
	HashMap<String, Object> Levelsmetadata =
            new HashMap<String, Object>();
	HashMap<String, Object> Upgradesmetadata =
            new HashMap<String, Object>();
   
    public LoadKingdoms(UUID kingdomid) {
    	this.instance = LevelsKingdoms.getInstance();
        this.uuid = kingdomid;
    }
    
    public Kingdom getKingdom(){
    	return Kingdom.getKingdom(this.uuid);
    }
    
    public String getKingdomName(){
    	return Kingdom.getKingdom(uuid).getName();
    	   
	}
	
	public void canUseItem(boolean use){
			this.canUseItem = use;
	}

	public boolean getCanUseItem(){
		return this.canUseItem;
	}

	public void CanUgradeNextLevel(boolean use){
			this.CanUgradeNextLevel = use;
	}

	public boolean getUgradeNextLevel(){
		return this.CanUgradeNextLevel;
	}

	public RotateHolograms hologram(){
		return this.rotatehologram;
	}
    
    public boolean checkdata(String name){
    	if (this.getAllMeta().containsKey(name)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public KingdomMetadata getMetaValue(String name){
    	if (this.checkdata(name)){
    		return this.getAllMeta().get(name);
    	}
		return null;
    }
    
    public Map<String, KingdomMetadata> getAllMeta(){
    	return Kingdom.getKingdom(uuid).getMetadata();
    }
   
    public void addlevels() {
    	this.level += 1;
    	Levelsmetadata.replace("level", this.level);
    }
   
    public int getlevel() { 
    	return this.level;
    }
   
    public void setlevels(int i) {
    	this.level = i;
    	Levelsmetadata.replace("level", this.level); 
   }
    
   public void createKingdomInfo(){
		
	    getKingdom().addMetadata("levels", Levelsmetadata);
		Levelsmetadata.put("level", 0);
		
		
		Upgradesmetadata.put("discord", false);
		getKingdom().addMetadata("upgrades", Upgradesmetadata);
   }
   
   public void getKingdomInfo(){
	   
		MetadataKingdom LevelMetadata = MetadataKingdom.getMetadata(this.getMetaValue("levels").getString());
	    
	    this.level = (int) Math.round(LevelMetadata.getDouble("level"));
	    
	    Levelsmetadata.put("level",  this.level);
	    
   }
   
	public LevelUpgrade getLevelUpgrades() {
		Kingdom kingdom = Kingdom.getKingdom(uuid);
		if (levelupgarde == null){
			setLevelUpgrades(new LevelUpgrade(kingdom, this));
		}
		return levelupgarde;
	}
	
	public DiscordKingdoms getdiscordKingdoms(){
		if (discordKingdoms == null){
			setdiscordKingdoms(new DiscordKingdoms(this));
		}
		
		return discordKingdoms;
	}
	
	public void setdiscordKingdoms(DiscordKingdoms discordKingdoms){
		this.discordKingdoms = discordKingdoms;
	}

	public void setLevelUpgrades(LevelUpgrade levelupgarde) {
		this.levelupgarde = levelupgarde;
	}
	
	public void saveMetaData(){
		getKingdom().addMetadata("levels", Levelsmetadata);
		getKingdom().addMetadata("upgrades", Upgradesmetadata);
	}
}
