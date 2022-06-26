package me.nostyll.Kingdoms.levels.gui.adminMode;

import java.util.concurrent.CompletableFuture;

import com.cryptomorin.xseries.XMaterial;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.DefaultKingdomPermission;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.gui.GUIOption;
import org.kingdoms.gui.GUIParser;
import org.kingdoms.gui.InteractiveGUI;
import org.kingdoms.main.locale.KingdomsLang;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.gui.Menus;
import net.wesjd.anvilgui.AnvilGUI;

public class AdminItemView {

    public static InteractiveGUI openItemsViewer(final Player player, final KingdomPlayer kp, final Kingdom kingdom) {
        OfflinePlayer placeholder = kp.getOfflinePlayer();
        if (!kp.isAdmin()) {
            DefaultKingdomPermission.USE.sendDeniedMessage(player);
            return null;
        }else if (kingdom != null){
            KingdomsLang.NO_KINGDOM_DEFAULT.sendMessage(player, new Object[0]);
            return null;
        }
        else {
            InteractiveGUI gui = GUIParser.parse(player, placeholder, "admin", new Object[0]);
            if (gui == null) {
                return null;
            }
            else {
                gui.push("itemviewer", () -> {
                    //openItemSelector(player, kp, kingdom);
                });
                gui.openInventory();
                return gui;
            }
        }
    }

    public void openAnvil(Player myplayer){
        XMaterial first = XMaterial.GOLD_BLOCK;
        XMaterial second = XMaterial.IRON_INGOT;

        new AnvilGUI.Builder()
        .onClose(player -> {                      //called when the inventory is closing
            player.sendMessage("You closed the inventory.");
        })
        .onComplete((player, text) -> {           //called when the inventory output slot is clicked
            if(text.equalsIgnoreCase("you")) {
                player.sendMessage("You have magical powers!");
                return AnvilGUI.Response.close();
            } else {
                return AnvilGUI.Response.text("Incorrect.");
            }
        })
        .preventClose()                           //prevents the inventory from being closed
        .text("What is the meaning of life?")     //sets the text the GUI should start with
        .itemLeft(first.parseItem())   //use a custom item for the first slot
        .itemRight(second.parseItem())  //use a custom item for the second slot
        .onLeftInputClick(p->p.sendMessage("sword"))    //called when the left input slot is clicked
        .onRightInputClick(p->p.sendMessage("ingot"))   //called when the right input slot is clicked
        .title("Enter your answer.")              //set the title of the GUI (only works in 1.14+)
        .plugin(LevelsKingdoms.getInstance())                 //set the plugin instance
        .open(myplayer);                          //opens the GUI for the player provided
    }
    
}
