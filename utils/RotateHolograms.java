package me.nostyll.Kingdoms.levels.utils;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;

public class RotateHolograms{
	
	BukkitTask runnable = null;
	Hologram holo = null;

	public Location getLocationAroundCircle(Location center, double radius, double angleInRadian) {
        double x = center.getX() + radius * Math.cos(angleInRadian);
        double z = center.getZ() + radius * Math.sin(angleInRadian);
        double y = center.getY();

        Location loc = new Location(center.getWorld(), x, y, z);
        Vector difference = center.toVector().clone().subtract(loc.toVector()); // this sets the returned location's direction toward the center of the circle
        loc.setDirection(difference);

        return loc;
    }
	
	public void startRotation(Location centerloc, Hologram hologram,  int time){
        final Location center = centerloc;
        final double radius = 1.5;
        final double radPerSec = 3.5;
        final double radPerTick = radPerSec / 40f;
        holo = hologram;

        runnable = new BukkitRunnable() {
            int tick = 0;
            public void run() {
                ++tick;
                	Location loc = getLocationAroundCircle(center, radius, radPerTick * tick);
                	hologram.teleport(loc);
                	
            }
        }.runTaskTimer(LevelsKingdoms.getInstance(), 0, 1);
	}
	
	public void stopTask(){
		this.runnable.cancel();
		holo.delete();
	}
	
}
