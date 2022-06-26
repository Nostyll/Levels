package me.nostyll.Kingdoms.levels.gui.adminMode;

import java.util.Arrays;
import java.util.Map;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.DefaultKingdomPermission;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.gui.GUIParser;
import org.kingdoms.gui.InteractiveGUI;
import org.kingdoms.main.locale.KingdomsLang;
import org.kingdoms.main.locale.MessageHandler;
import org.kingdoms.utils.MathUtils;
import org.kingdoms.utils.xseries.XItemStack;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.game.AdminManager;
import me.nostyll.Kingdoms.levels.game.GameManagement;
import me.nostyll.Kingdoms.levels.gui.Menus;
import me.nostyll.Kingdoms.levels.handler.levels.LevelsConfigLoader;

public class AdminModeGui {


    public static InteractiveGUI openAdminMenu(final Player player, final KingdomPlayer kp) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)LevelsKingdoms.getInstance(), () -> {
            AdminManager adminmodeManager = GameManagement.getadminManager();
            Kingdom kingdom = kp.getKingdom();
            if (!kp.isAdmin()) {
                DefaultKingdomPermission.USE.sendDeniedMessage(player);
                return;
            }else if (kingdom == null){
                KingdomsLang.NO_KINGDOM_DEFAULT.sendMessage(player, new Object[0]);
                return;
            }
            else {
            InteractiveGUI adminMode = GUIParser.parse(player, kp.getOfflinePlayer(), Menus.ADMIN.getMenuName(),
            Arrays.asList(new Object[] { 
                "ActiveMode", MessageHandler.colorize(adminmodeManager.isUserEditing(player) ? "&aEnabled" : "&cDisabled")}));
            if (adminMode == null) {
                return;
            }

            adminMode.push("edit",() -> {
                if (!adminmodeManager.isUserEditing(player)){
                    adminmodeManager.addUserEditing(player, 0);
                    openAdminMenu(player, kp);
                }else{
                    adminmodeManager.removeUserEditing(player);
                    openAdminMenu(player, kp);
                }
            }, new Object[0]);

            adminMode.push("item", () -> {
                openLevelSelector(player, kp, 0);
            }, new Object[0]);
            adminMode.openInventory();
        }
         });
         return null;
    }

    public static void openLevelSelector(final Player player, final KingdomPlayer kp, int page) {
        Bukkit.getScheduler().runTaskAsynchronously(LevelsKingdoms.getInstance(), () -> {
            LevelsConfigLoader itemconfig = LevelsKingdoms.getLevelsConfigLoader();
            InteractiveGUI gui = GUIParser.parse(player, kp.getOfflinePlayer(), Menus.ADMIN_HologramSelector.getMenuName() , new Object[] { "page", Integer.valueOf(page + 1) });
            gui.push("back",  () -> {}, new Object[0]);
            InteractiveGUI.OptionBuilder holder = gui.getReusableOption("holograms");
            int eachPage = holder.slotsCount();
            int maxPages = MathUtils.getPageNumbers(itemconfig.levels().keySet().size(), eachPage);
            gui.addEdit("pages", Integer.valueOf(maxPages));
            gui.push("next-page", () -> {}, new Object[0]);
            gui.push("previous-page", () -> {}, new Object[0]);
            gui.push("back", () -> {
                openAdminMenu(player, kp);
            }, new Object[0]);
            
            for (Object levelsList : itemconfig.levels().keySet().stream().skip((page * eachPage)).limit(eachPage).toArray()){
              OfflinePlayer placeholder = kp.getOfflinePlayer();
              holder.push(() -> {
                  openLevel(player, kp, placeholder, Integer.parseInt(levelsList.toString()), page);
              }, new Object[] { "level_name", MessageHandler.colorize("&b&l" + levelsList.toString()) });
            } 
            gui.openInventory();
        });
    }

    public static void openLevel(Player player, KingdomPlayer kp, OfflinePlayer placeholder, int level, int page) { 
        Bukkit.getScheduler().runTaskAsynchronously(LevelsKingdoms.getInstance(), () -> {
            InteractiveGUI levelGUI = GUIParser.parse(player, kp.getOfflinePlayer(), Menus.ADMIN_LEVELS.getMenuName(), new Object[] {"level", MessageHandler.colorize("&b&l"+ level )});
            LevelsConfigLoader levelsloader = LevelsKingdoms.getLevelsConfigLoader();
            ItemStack Finalmat = levelsloader.getConfig(level).getFinalItem();
            InteractiveGUI.OptionBuilder Final = levelGUI.getReusableOption("final");
            InteractiveGUI.OptionBuilder Base = levelGUI.getReusableOption("base");
            InteractiveGUI.OptionBuilder edit = levelGUI.getReusableOption("edit_holo");

            edit.modifyItem(item -> {
                if (level == 1){
                    item.setType(XMaterial.BARRIER.parseMaterial());
                }
            });

            Final.modifyItem(item -> { 
                XMaterial.matchXMaterial(Finalmat).setType(item);
                item.setItemMeta(Finalmat.getItemMeta());
            });
            Final.push(() -> {
                openItemEdit(player, kp, placeholder, kp, placeholder, page, level, true, 0);
            }, new Object[0] );

            levelsloader.getConfig(level).getBasesHashMap().forEach((id, itemstack) -> {
                Base.modifyItem(item ->  {
                    XMaterial.matchXMaterial(itemstack).setType(item);
                    item.setItemMeta(itemstack.getItemMeta());
                });
                Base.push(()-> {
                    openItemEdit(player, kp, placeholder, kp, placeholder, page, level, false, id);
                } ,new Object[0]);
            });
            levelGUI.push("back", () -> openLevelSelector(player, kp, page), new Object[0]);
            levelGUI.push("edit_holo", () -> {
                if (level == 1) return;
                if (!XMaterial.STICK.isSimilar(Finalmat) && GameManagement.getadminManager().isUserEditing(player)){
                    GameManagement.getadminManager().addUserEditing(player, level);
                    GameManagement.getadminManager().PreviewLevels(player);
                    ActionBar.sendActionBarWhile(LevelsKingdoms.getInstance(), player, ChatColor.DARK_GREEN + "EdittingMode: Active, exit Shift+Left click", () -> GameManagement.getadminManager().isUserEditing(player));
                    player.closeInventory();
                    return;
                }
            }, new Object[0]);
            levelGUI.openInventory();
        });
      }


      public static void openItemEdit(Player player, KingdomPlayer kp, OfflinePlayer placeholder, KingdomPlayer member, OfflinePlayer memberPlayer, int page, int level, boolean finalItem, int base) {
        InteractiveGUI ItemEditGUI = GUIParser.parse(player, kp.getOfflinePlayer(), Menus.ADMIN_CONFIRM_EDIT_ITEMS.getMenuName(), new Object[0]);
        LevelsConfigLoader levelsloader = LevelsKingdoms.getLevelsConfigLoader();
        ItemEditGUI.onDelayedInteractableSlot(interactEvent -> {
            Map<Integer, ItemStack> items = ItemEditGUI.getInteractableMap(true);
            if (items.isEmpty())
              return; 
            player.getOpenInventory().getTopInventory().setItem(((Integer)items.keySet().iterator().next()).intValue(), null);
            player.updateInventory();
            ItemStack Newitem = items.values().iterator().next();
            if (Newitem.getAmount() > 1) {
                Newitem.setAmount(Newitem.getAmount() - 1);
              XItemStack.giveOrDrop(player, new ItemStack[] { Newitem });
            } 
            if (finalItem){
                levelsloader.getConfig(level).setFinalItem(Newitem);
               }else{
                levelsloader.getConfig(level).setBase(base, Newitem);
               }
            ActionBar.sendActionBar(LevelsKingdoms.getInstance(), player, ChatColor.DARK_PURPLE + "New item added!", 4*20);
          });

        ItemEditGUI.push("back", () -> openLevel(player, kp, placeholder, level, page), new Object[0]);
        ItemEditGUI.openInventory();
      }
}
