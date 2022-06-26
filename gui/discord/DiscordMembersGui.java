package me.nostyll.Kingdoms.levels.gui.discord;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.gui.GUIParser;
import org.kingdoms.gui.InteractiveGUI;
import org.kingdoms.main.locale.KingdomsLang;
import org.kingdoms.main.locale.MessageHandler;
import org.kingdoms.utils.MathUtils;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.gui.Menus;
import me.spomg.plugins.api.minecord.MAPI;
import net.dv8tion.jda.api.entities.Member;

public class DiscordMembersGui {

	

    public static void openMembers(KingdomPlayer kpMain, Player player, OfflinePlayer placeholder, Kingdom kingdom, int page) {
        Bukkit.getScheduler().runTaskAsynchronously(LevelsKingdoms.getInstance(), () -> {
            MAPI minecord = LevelsKingdoms.getInstance().getServer().getServicesManager().load(MAPI.class);
        	InteractiveGUI gui = GUIParser.parse(player, kpMain.getOfflinePlayer(), Menus.DISCORD_MEMBERS.getMenuName(),
            Arrays.asList(new Object[] {  
                "page", Integer.valueOf(page + 1) }));
            InteractiveGUI.OptionBuilder holder = gui.getReusableOption("members");
            KingdomPlayer kp = KingdomPlayer.getKingdomPlayer((OfflinePlayer)player);
            List<KingdomPlayer> members = kingdom.getKingdomPlayers();
            int eachPage = holder.slotsCount();
            int maxPages = MathUtils.getPageNumbers(members.size(), eachPage);
            gui.addEdit("pages", Integer.valueOf(maxPages));
            gui.push("next-page", () -> {}, new Object[0]);
            gui.push("previous-page", () -> {}, new Object[0]);
            
            gui.push("back", () -> DiscordMenuGui.discordmenu(player, kp, kingdom), new Object[0]);
            members.forEach(kplayer -> {
                Player user = kplayer.getPlayer();
                if (minecord.isPlayerLinked(user)){
                    Member discordmember = minecord.getMemberFromLinkedPlayer(user);
                    LevelsKingdoms.logInfo("TEST: " + discordmember.getId());
                }
            });
            
             /* for (KingdomPlayer member : kingdom.getKingdomPlayers()) {
            	OfflinePlayer memberPlayer = member.getOfflinePlayer();
            	Member discordmember = minecord.getMemberFromLinkedPlayer(memberPlayer.getPlayer());
                holder.pushHead(memberPlayer, ()-> {
                    openMember(player, kingdom, kp, memberPlayer, member, memberPlayer, maxPages);
                }, new Object[] { 
                "status", MessageHandler.colorize(discordmember != null ? "&fOnline status :" + discordmember.getOnlineStatus() : "&cNot linked"),
                "link", MessageHandler.colorize(minecord.isPlayerLinked(memberPlayer.getPlayer()) ? "&2Linked" : "&cNot linked"), 
                "discord_joined", 
                MessageHandler.colorize(minecord.getMemberFromLinkedPlayer(memberPlayer.getPlayer()) != null ? "&b" + minecord.getMemberFromLinkedPlayer(memberPlayer.getPlayer()).getUser() : "&4Not Linked")}
                );
            } */ 
            gui.openInventory();
        });
    }
    
    public static void openMember(final Player player, final Kingdom kingdom, final KingdomPlayer kp, OfflinePlayer placeholder, final KingdomPlayer member, final OfflinePlayer memberPlayer, int page) {
        if (!kp.isAdmin() && !kingdom.isMember(kp)) {
            KingdomsLang.NEXUS_MEMBERS_OTHERS.sendMessage(player, new Object[0]);
            player.closeInventory();
            return;
          } 
        InteractiveGUI memberGUI = GUIParser.parse(player, kp.getOfflinePlayer(), Menus.DISCORD_MEMBER.getMenuName(), new Object[0]);
        InteractiveGUI.OptionBuilder memberHead = memberGUI.getReusableOption("member");
        memberHead.pushHead(memberPlayer, () -> {
        
        }, new Object[] { "online", MessageHandler.colorize(memberPlayer.isOnline() ? "&2Online" : "&cOffline") });
        //memberGUI.push(option, item, holder.next(), () -> openMember(player, kingdom, kp, member, memberPlayer), new Object[0]);
        memberGUI.push("back", () -> openMembers(kp, player, placeholder, kingdom, page), new Object[0]);
        memberGUI.push("kick", () -> {
                return;
        }, new Object[0]).push("demote", () -> {
                return;
        }, new Object[0]).push("promote", () -> {
                return;
        }, new Object[0]);
        memberGUI.openInventory();
    }
    
}
