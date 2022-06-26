package me.nostyll.Kingdoms.levels.game;

import org.bukkit.plugin.Plugin;
import org.kingdoms.gui.GUIConfig;
import org.kingdoms.gui.InteractiveGUI;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.gui.Menus;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;

public class MenuMananger extends Manager {

    InteractiveGUI levelsgui;
    LevelsKingdoms plugin = LevelsKingdoms.getInstance();

    protected MenuMananger(Plugin plugin) {
        super(plugin);
        this.buildMenus();
    }

    public void buildMenus(){
        Menus.streams().forEach((menu) -> {
            GUIConfig.registerGUI(plugin, menu.getMenuName(), menu.getMenuPath());
        });
	}

    public void reloadAll(){
        Menus.streams().forEach((menu) -> {
            GUIConfig.reload(menu.getMenuName());
        });
    }

    @Override
    public void onDisable() {

    }
    
}
