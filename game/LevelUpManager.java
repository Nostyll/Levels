package me.nostyll.Kingdoms.levels.game;

import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.land.Land;
import org.kingdoms.constants.land.structures.Structure;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.events.lands.LandLoadEvent;

import com.cryptomorin.xseries.messages.Titles;
import com.cryptomorin.xseries.XMaterial;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CloudEffect;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.WarpEffect;
import de.slikey.effectlib.util.DynamicLocation;
import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import me.nostyll.Kingdoms.levels.events.LoadLevelEvent;
import me.nostyll.Kingdoms.levels.external.ExternalManager;
import me.nostyll.Kingdoms.levels.handler.LoadKingdoms;
import me.nostyll.Kingdoms.levels.handler.items.ItemLoader;
import me.nostyll.Kingdoms.levels.handler.levels.LevelsConfigLoader;
import me.nostyll.Kingdoms.levels.handler.manager.Manager;
import me.nostyll.Kingdoms.levels.utils.Discord;

public class LevelUpManager extends Manager implements Listener{
	
	EffectManager effect;
	LevelsConfigLoader levelsloader = LevelsKingdoms.getLevelsConfigLoader();
	int MaxLevelFiles = LevelsKingdoms.config.maxLevels;
	
	public LevelUpManager(Plugin plugin){
		super(plugin);
		effect = new EffectManager(plugin);
		
	}
	
	public int getLevel(Kingdom kingdom){
		if (GameManagement.getKingdomManager().getLoadKingdomsData().hasKingdomData(kingdom.getId())){
			LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
			return leveldata.getlevel();	
		}else{
			return 0;
		}
	}
	
	public int getLevel(Location loc){

		Land land = Land.getLand(loc);
		Kingdom kingdominfo = land.getKingdom();
		
		if (kingdominfo == null) return 0;
		
		LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdominfo.getId());
		
		return leveldata.getlevel();
	}
	
	public void setLevel(Kingdom kingdom){
		
		if (kingdom == null){
			LevelsKingdoms.logInfo("Empty data:");
			LevelsKingdoms.logInfo("Kingdom" + kingdom);
			LevelsKingdoms.logInfo("Class: [LevelUpManager]  function : setLevel()");
		}
		
		LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
		if (leveldata.getlevel() < MaxLevelFiles){ 
			leveldata.addlevels();
			Bukkit.getScheduler().runTaskAsynchronously(LevelsKingdoms.getInstance(), () -> {
				Bukkit.getPluginManager().callEvent(new LoadLevelEvent(leveldata, kingdom, true));
			});
		}
	}
	
	
	
	public void CreateLevelup(Player player, Kingdom kingdom){
		Location location = kingdom.getNexus().toBukkitLocation();
		LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
		KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player.getUniqueId());
		int level = getLevel(kp.getKingdom());
		Location levelLocation = location.clone().add(0, 1, 0);
		
		WarpEffect Warp = new WarpEffect(effect);

		getUpgradeMsg(player);
		
		Warp.asynchronous = true;
		Warp.autoOrient = true;
		Warp.particle = Particle.SPELL_WITCH;
		Warp.radius= 1;
		Warp.rings = 10;
		Warp.grow = .1f;
		Warp.particles = 25; 
		Warp.setLocation(location.clone().add(0.5, 0, 0.5));
		
		Warp.callback = new Runnable() {

            @Override
            public void run() {
				
				ItemLoader item = levelsloader.getConfig(leveldata.getlevel());

				if (level == 0){
					setLevel(kingdom);
					placelevel(getLevel(location.clone()), kp, location, true);
					playSmoke(location.clone());
				}else{
					setLevel(kingdom);
					placelevel(getLevel(location.clone()), kp, location, true);               		
					Location hologramloc1 = location.clone().add(item.getX(), item.getY(), item.getZ());
					playLine(levelLocation, hologramloc1);
				}
				Titles.clearTitle(player);
				Titles.sendTitle(player, 
						5, (3 * 20), 20, 
						ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Upgrading...Done!",
						ChatColor.YELLOW + "" + ChatColor.BOLD + "Upgraded your nexus!");
            }

        };
        // Smoke takes 10 seconds
        // period * iterations = time of effect
        Warp.iterations = 10 * 20;
        Warp.start();
        
	}
	
	public void playSmoke(Location location){
		CloudEffect Smoke = new CloudEffect (effect);
		
		Smoke.asynchronous = true;
		Smoke.autoOrient = true;
		Smoke.cloudSize = 0.8f;
		Smoke.mainParticle = Particle.CLOUD;
		Smoke.setLocation(location.clone().add(0.5, 0, 0.5));
		Smoke.yOffset = 0;
		
        Smoke.iterations = 1 * 20;
        Smoke.start();
	}
	
	public void playLine(Location location, Location target){
		LineEffect Line = new LineEffect (effect);
		DynamicLocation nexusloc = new DynamicLocation(location.clone().add(0.5, 1.0, -0.5));
		DynamicLocation targetloc= new DynamicLocation(target.clone().add(0.5, 1.0, 0.0)); 
		
		Line.asynchronous = true;
		Line.autoOrient = true;
		Line.particle = Particle.ENCHANTMENT_TABLE;
		Line.particles = 25;
		Line.setDynamicOrigin(nexusloc);
		Line.setDynamicTarget(targetloc);
		
        // Line takes 15 seconds
        // period * iterations = time of effect
		Line.iterations = 1 * 20;
		Line.start();
	}
	
	public void placelevel(int Level, KingdomPlayer kp, Location loc, boolean checknormalupgrade){
		
		Land land = Land.getLand(loc);
		Kingdom kingdom = land.getKingdom();
		
		if (kingdom == null)return;

		LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kingdom.getId());
		ItemLoader item = levelsloader.getConfig(leveldata.getlevel());
		Location nexusloc = kingdom.getNexus().toBukkitLocation();
		
		if (checknormalupgrade == true){
			setLevelMessage(kingdom);
			leveldata.CanUgradeNextLevel(true);
		}

		if (Level == 1){
			placeLevel(nexusloc.clone().add(item.getX(), item.getY(), item.getZ()), kp);
		}else{
			placeHologram(nexusloc.clone().add(item.getX(), item.getY(), item.getZ()), item.getIcon());
		}	
	}
	
    public void placeLevel(final Location loc, final KingdomPlayer kp) {
		LoadKingdoms leveldata = GameManagement.getKingdomManager().getLoadKingdomsData().getKingdomData(kp.getKingdom().getId());
		ItemLoader item = levelsloader.getConfig(leveldata.getlevel());
        final XMaterial mat = item.getIcon();
        final Material material = mat.parseMaterial();
        final Block block = loc.getBlock();
        block.setType(material);
        block.setMetadata(GameManagement.getLevelNexusManager().nexuslevel, new FixedMetadataValue(LevelsKingdoms.getInstance(), kp.getKingdomId()));
    }
	
	
	public void setNexuslevelfirst(Kingdom kingdom, KingdomPlayer kp, Location target){
		
		int valueToMatch = this.getLevel(kingdom);
		
		IntStream.rangeClosed(0, LevelsKingdoms.config.maxLevels).map(level -> level + 1).limit(valueToMatch).forEach(
				(levels) -> {
					placelevel(levels, kp, target, false);
				}
			);
	}
	
	public void removeHoloAndLevel(Kingdom kingdom){
		Location loc = kingdom.getNexus().toBukkitLocation().clone().add(0, 1, 0);

		IntStream.rangeClosed(0, MaxLevelFiles)
		.map(File -> File + 1)
		.limit(MaxLevelFiles)
		.forEach((Files) -> {
			ItemLoader item = levelsloader.getConfig(Files);
			if (getLevel(kingdom) >= 1){
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
			}
		});
	}
	
	public void setLevelMessage(Kingdom kingdom){

		Bukkit.getScheduler().runTaskLater(LevelsKingdoms.getInstance(), () -> {
    		kingdom.getKingdomPlayers().forEach((Kplayer) -> {
				Titles.clearTitle(Kplayer.getPlayer());
				Titles.sendTitle(Kplayer.getPlayer(), 
						5, (3 * 20), 20, 
						ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Nexus Levelup",
						ChatColor.WHITE + "" + ChatColor.BOLD + "Your nexus is now level " + this.getLevel(kingdom));
				ExternalManager.getDiscordSRVManager().SentMessageToDiscordUser(Kplayer.getPlayer(), Discord.BuildLevelMessage(), "");
    		} );
		}, 2 * 20);
	}

	public void getUpgradeMsg(Player player){
		Titles.clearTitle(player);
		Titles.sendTitle(player, 
				5, (14 * 20), 20, 
				ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Upgrading...",
				ChatColor.YELLOW + "" + ChatColor.BOLD + "Upgrading your nexus!");
	}
	
	public void placeHologram(Location loc, XMaterial item){
		ItemStack itemStack = item.parseItem();
		Hologram hologram = HologramsAPI.createHologram(LevelsKingdoms.getInstance(), loc);
		hologram.appendItemLine(itemStack);
		VisibilityManager visiblityManager = hologram.getVisibilityManager();
		visiblityManager.setVisibleByDefault(true);
		visiblityManager.resetVisibilityAll();
	}
	
	@EventHandler
	public void Loadlevels(LandLoadEvent event){
	    Land land = event.getLand();
	    if (land == null) return;
		Kingdom Kingdom = land.getKingdom();
		if (Kingdom == null) return;
		KingdomPlayer kp = event.getLand().getKingdom().getKing();
		if (!kp.hasKingdom()) return;
		Structure structure = land.getStructures().get(Kingdom.getNexus());
		if (structure == null) return;
		Bukkit.getScheduler().runTask(LevelsKingdoms.getInstance(), () -> {
			if (structure != null && structure.getStyle().getType().isNexus()) {
				if (getLevel(Kingdom) == 0) {
					if (land.isNexusLand() && structure.getStyle().getType().isNexus()){
						structure.updateHolograms(Kingdom);
					}
				}else{
					structure.removeHolograms();
					setNexuslevelfirst(Kingdom, kp, Kingdom.getNexus().toBukkitLocation());
				}
			}
		});
	}

	@Override
	public void onDisable() {
		effect.dispose();
		effect = null;	
	}

}
