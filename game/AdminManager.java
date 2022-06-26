package me.nostyll.Kingdoms.levels.game;

import java.util.HashMap;
import java.util.stream.IntStream;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.land.Land;
import org.kingdoms.constants.player.KingdomPlayer;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.handler.items.ItemLoader;
import me.nostyll.Kingdoms.levels.handler.levels.LevelsConfigLoader;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;

public class AdminManager extends Manager implements Listener {

    public HashMap<Player, Integer> users = new HashMap<Player, Integer>();
    public HashMap<Player, Hologram> holograms = new HashMap<Player, Hologram>();
    double Blockmove = 0.1;

    protected AdminManager(Plugin plugin) {
        super(plugin);
    }

    public void onSneakUpdate(Player player, boolean Increase){
        double blockmoveCal = ((double)Math.round(Blockmove * 10) / 10);
        if (Increase){
            double newBlockMove = blockmoveCal + 0.1;
            if (newBlockMove <= 1.0)
            Blockmove = Blockmove + 0.1;
        }else{
            double newBlockMove = blockmoveCal - 0.1;
            if (newBlockMove > 0.0)
            Blockmove = Blockmove - 0.1;
        }

        if (blockmoveCal == 1.0){
            Titles.clearTitle(player);
            Titles.sendTitle(player, 
                    5, (3 * 20), 20, 
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "Max Blocks Reach",
                    ChatColor.WHITE + "" + ChatColor.BOLD + "Max amount of blocks is, 1.0 full block.");
        }
        if (blockmoveCal == 0.1){
            Titles.clearTitle(player);
            Titles.sendTitle(player, 
                    5, (3 * 20), 20, 
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "Min Blocks Reach",
                    ChatColor.WHITE + "" + ChatColor.BOLD + "Min amount of blocks is, 0.1 block.");
        }
        Titles.clearTitle(player);
        Titles.sendTitle(player, 
                5, (3 * 20), 20, 
                ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Blocks Reach",
                ChatColor.WHITE + "" + ChatColor.BOLD + blockmoveCal +"/1.0");
    }

    @EventHandler
    public void PlayerItemHeldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        KingdomPlayer kplayer = KingdomPlayer.getKingdomPlayer(player);
        int newSlot = event.getNewSlot();
        int oldSlot = event.getPreviousSlot();
        double blockmoveCal = ((double)Math.round(Blockmove * 10) / 10);
        if (kplayer.isAdmin() && isUserEditing(player)){

            ItemLoader holoLocation = LevelsConfigLoader.getLevelsLoader().getConfig(getEditLevel(player));

            if (oldSlot == 1 && newSlot == 0){
                if (player.isSneaking()){
                    onSneakUpdate(player, false);
                    event.setCancelled(true);
                }else{
                    //X -
                    holoLocation.setX(holoLocation.getX() - blockmoveCal);
                    LevelsKingdoms.logDebug("X-" + holoLocation.getX());
                    updateHologram(player, holoLocation.getX(), holoLocation.getY(), holoLocation.getZ());
                    event.setCancelled(true);
                }

            }
            if (oldSlot == 1 && newSlot == 2){
                if (player.isSneaking()){
                    onSneakUpdate(player, true);
                    event.setCancelled(true);
                }else{
                    //X + 
                    holoLocation.setX(holoLocation.getX() + blockmoveCal);
                    LevelsKingdoms.logDebug("X+" + holoLocation.getX());
                    updateHologram(player, holoLocation.getX(), holoLocation.getY(), holoLocation.getZ());
                    event.setCancelled(true);
                }

            }

            if (oldSlot == 4 && newSlot == 3){
                if (player.isSneaking()){
                    onSneakUpdate(player, false);
                    event.setCancelled(true);
                }else{
                    //Y - 
                    holoLocation.setY(holoLocation.getY() - blockmoveCal);
                    LevelsKingdoms.logDebug("Y-" + holoLocation.getY());
                    updateHologram(player, holoLocation.getX(), holoLocation.getY() - blockmoveCal, holoLocation.getZ());
                    event.setCancelled(true);
                }
            }
    
            if (oldSlot == 4 && newSlot == 5){
                if (player.isSneaking()){
                    onSneakUpdate(player, true);
                    event.setCancelled(true);
                }else{
                    //Y + 
                    holoLocation.setY(holoLocation.getY() + blockmoveCal);
                    LevelsKingdoms.logDebug("Y+" + holoLocation.getY());
                    updateHologram(player, holoLocation.getX(), holoLocation.getY() + blockmoveCal, holoLocation.getZ());
                    event.setCancelled(true);   
                }
            }
    
            if (oldSlot == 7 && newSlot == 6){
                if (player.isSneaking()){
                    onSneakUpdate(player, false);
                    event.setCancelled(true);
                }else{ 
                    //Z -
                    holoLocation.setZ(holoLocation.getZ() - blockmoveCal);
                    LevelsKingdoms.logDebug("Z-" + holoLocation.getZ());
                    updateHologram(player, holoLocation.getX(), holoLocation.getY(), holoLocation.getZ());
                    event.setCancelled(true);}
            }
            if (oldSlot == 7 && newSlot == 8){
                if (player.isSneaking()){
                    onSneakUpdate(player, true);
                    event.setCancelled(true);
                }else{
                    //Z +
                    holoLocation.setZ(holoLocation.getZ() + blockmoveCal);
                    LevelsKingdoms.logDebug("Z+" + holoLocation.getZ());
                    updateHologram(player, holoLocation.getX(), holoLocation.getY(), holoLocation.getZ());
                    event.setCancelled(true);    
                }   
            }
        }
    }

    @EventHandler
    public void PlayerLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isUserEditing(player)){
            removeUserEditing(player);
            holograms.remove(player);
        }
    }


    public void updateHologram(Player player, Double x, Double y, Double z){
        Hologram hologram = holograms.get(player);
        Location newloc = hologram.getLocation().clone().add(x, y, z);
		hologram.teleport(newloc);
	}

    public void PreviewLevels(Player player){

        int max = this.getEditLevel(player);
        KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
		
		IntStream.rangeClosed(0, LevelsKingdoms.config.maxLevels).map(level -> level + 1).limit(max).forEach(
				(levels) -> {
					placelevel(levels, kp, kp.getKingdom().getNexus().toBukkitLocation());
				}
			);
    }

    public void placelevel(int Level, KingdomPlayer kp, Location loc){
		
		Land land = Land.getLand(loc);
		Kingdom kingdom = land.getKingdom();
        LevelsConfigLoader levelsloader = LevelsKingdoms.getLevelsConfigLoader();
		
		Location nexusloc = kingdom.getNexus().toBukkitLocation();
        ItemLoader item = levelsloader.getConfig(Level);

		if (Level == 1){
			placeLevel(nexusloc.clone().add(item.getX(), 1, item.getZ()), kp, Level);
		}else{
			placeHologram(nexusloc.clone().add(item.getX(), item.getY(), item.getZ()), kp.getPlayer(), item.getIcon());
		}	
	}

    public void placeHologram(Location loc, Player player, XMaterial item){
		ItemStack itemStack = item.parseItem();
		Hologram hologram = HologramsAPI.createHologram(LevelsKingdoms.getInstance(), loc);
		hologram.appendItemLine(itemStack);
		VisibilityManager visiblityManager = hologram.getVisibilityManager();
		visiblityManager.setVisibleByDefault(true);
		visiblityManager.resetVisibilityAll();
        holograms.put(player, hologram);

	}

    public void placeLevel(final Location loc, final KingdomPlayer kp, int level) {
        LevelsConfigLoader levelsloader = LevelsKingdoms.getLevelsConfigLoader();
		ItemLoader item = levelsloader.getConfig(level);
        final Material material = item.getIcon().parseMaterial();
        final Block block = loc.getBlock();
        block.setType(material);
        block.setMetadata(GameManagement.getLevelNexusManager().nexuslevel, new FixedMetadataValue(LevelsKingdoms.getInstance(), kp.getKingdomId()));
    }
    

    public void RemovePreview(){

    }

    public void RemovePreview(Player player){
        KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
		Location loc = kp.getKingdom().getNexus().toBukkitLocation().clone().add(0, 1, 0);
        int max = this.getEditLevel(player);
        LevelsConfigLoader levelsloader = LevelsKingdoms.getLevelsConfigLoader();
		IntStream.rangeClosed(0, LevelsKingdoms.config.maxLevels)
		.map(level -> level + 1)
		.limit(max)
		.forEach((levels) -> {
            ItemLoader item = levelsloader.getConfig(levels);
				HologramsAPI.getHolograms(LevelsKingdoms.getInstance()).forEach(h ->{
					LevelsKingdoms.newChain()
					.sync(() -> {
						if (h.getLocation().getX() == item.getX() && 
						h.getLocation().getY() == item.getY() &&
						h.getLocation().getZ() == item.getZ()){
						h.delete();
					}
					})
					.delay(1 * 20)
					.sync(() ->{
						loc.getBlock().setType(Material.AIR);
						loc.getBlock().removeMetadata(GameManagement.getLevelNexusManager().nexuslevel, LevelsKingdoms.getInstance());
					}).execute();
				});
		});
	}

    @EventHandler
    public void ExitEdittingMode(PlayerInteractEvent event){
		@NotNull Action action = event.getAction();
		Player player = event.getPlayer();
		
		KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer(player.getUniqueId());
		Kingdom kingdom = kingdomPlayer.getKingdom();
		
		if (player.isSneaking() && action == Action.LEFT_CLICK_AIR 
        || player.isSneaking() && action == Action.LEFT_CLICK_BLOCK){
                if (player.getGameMode() == GameMode.CREATIVE) return;
                if (kingdom != null && isUserEditing(player)){
                    removeUserEditing(player);
                    ActionBar.sendActionBar(player, ChatColor.DARK_RED + "You exit Editmode");

                }
        }

    }

    public HashMap<Player, Integer> getUsers() {
        return users;
    }

    public boolean isUserEditing(Player player) {
        if (getUsers().containsKey(player)){
            return true;
        }else{
            return false;
        }
    }

    public void removeUserEditing(Player player){
        if (isUserEditing(player)){
            getUsers().remove(player);
        }
    }

    public void addUserEditing(Player player, int level){
            getUsers().put(player, level);
    }

    public int getEditLevel(Player player){
        if (isUserEditing(player)){
            return getUsers().get(player);
        }
        return 0;
    }

    @Override
    public void onDisable() {

    }
    
}
