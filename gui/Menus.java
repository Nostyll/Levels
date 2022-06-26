package me.nostyll.Kingdoms.levels.gui;

import java.util.stream.Stream;

public enum Menus {

    LEVELS("guis/level/levels.yml"),
    LEVELS_UP("guis/level/levels_up.yml"),
    UPGRADES("guis/level/upgrades.yml"),
    DISCORD_MEMBERS("guis/level/discord/members.yml"),
    DISCORD_MEMBER("guis/level/discord/member.yml"),
    DISCORD("guis/level/discord/discord.yml"),
    ADMIN("guis/admin/admin.yml"),
    ADMIN_HologramSelector("guis/admin/hologramselector.yml"),
    ADMIN_LEVELS("guis/admin/levels.yml"),
    ADMIN_CONFIRM_EDIT_ITEMS("guis/admin/confirm/editItems.yml")
    ;

    private final String menuName;

    private Menus(String menu) {
        this.menuName = menu;
    } 
    
    public String getMenuPath() {
        return this.menuName;
    }
    
    public String getMenuName(){
        return this.menuName.toLowerCase();
    }

    public static Stream<Menus> streams(){
        return Stream.of(Menus.values());
    }
}
