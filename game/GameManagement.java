package me.nostyll.Kingdoms.levels.game;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.external.ExternalManager;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;

public class GameManagement extends Manager {
	
	
	private static ExternalManager<?> externalManager;
	private static GeneralAPIManager generalapiManager;
	private static KingdomManager kingdommanager;
	private static NexusManger nexusManger;
	private static LevelUpManager levelupManger;
	private static StrucutureManager strucutureManager;
	private static LevelUpItemManager levelupitemmanager;
	private static LevelNexusManager levelNexusManager;
	private static AdminManager adminManager;
	private static MenuMananger menuMananger;
	

	public GameManagement(LevelsKingdoms plugin) {
		super(plugin);
		
		externalManager = new ExternalManager<>(plugin);
		generalapiManager = new GeneralAPIManager(plugin);
		kingdommanager = new KingdomManager(plugin);
		nexusManger = new NexusManger(plugin);
		levelupManger = new LevelUpManager(plugin);
		strucutureManager = new StrucutureManager(plugin);
		levelupitemmanager = new LevelUpItemManager(plugin);
		levelNexusManager = new LevelNexusManager(plugin);
		adminManager = new AdminManager(plugin);
		menuMananger = new MenuMananger(plugin);
		
	
	}
	
	public static ExternalManager<?> getApiManager(){
		return externalManager;
	}
	
	public static GeneralAPIManager getGeneralApiManager(){
		return generalapiManager;
	}
	
	public static KingdomManager getKingdomManager(){
		return kingdommanager;
	}
	
	public static NexusManger getNexusManger(){
		return nexusManger;
	}
	
	public static LevelUpManager getLevelUpManager(){
		return levelupManger;
	}
	
	public static StrucutureManager getStructerManager(){
		return strucutureManager;
	}

	public static MenuMananger getMenuMananger(){
		return menuMananger;
	}
	
	public static LevelUpItemManager getLevelUpItemManager(){
		return levelupitemmanager;
	}
	
	public static LevelNexusManager getLevelNexusManager(){
		return levelNexusManager;
	}
	
	public static AdminManager getadminManager(){
		return adminManager;
	}
	
	@Override
	public void onDisable() {
		
	}

}
