package me.nostyll.Kingdoms.levels.handler.items;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.IntStream;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.nostyll.Kingdoms.levels.LevelsKingdoms;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ItemLoader {
	
	private Double locX = 0.0;
	private Double locY = 0.0;
	private Double locZ = 0.0;
	private int levelmoney_cost_level = 200;
	private int levelrp_cost_level = 500;

	private ItemStack Final = XMaterial.STICK.parseItem();

	private HashMap<Integer, ItemStack> base = new HashMap<Integer, ItemStack>();

	private FileConfiguration config;
	private File file;

	public ItemLoader(FileConfiguration levelFile, File file) {
		this.file = file;
		this.config = levelFile;
		load();
		save();
	}

	public void load() {
		this.locX = config.getDouble("Location.x");
		this.locY = config.getDouble("Location.y");
		this.locZ = config.getDouble("Location.z");
		this.levelmoney_cost_level = config.getInt("Level.money");
		this.levelrp_cost_level = config.getInt("Level.rp");
		if (config.getConfigurationSection("items.final") != null) {
			this.Final = XItemStack.deserialize(config.getConfigurationSection("items.final"));
		}
		IntStream.rangeClosed(1, LevelsKingdoms.getMax()).forEach(nbr -> {
			if (config.getConfigurationSection("items.base" + String.valueOf(nbr)) != null) {
				base.put(nbr, XItemStack.deserialize(config.getConfigurationSection("items.base" + String.valueOf(nbr))));
			}
		});
	}

	public void save() {
		config.set("Location.x", this.locX);
		config.set("Location.y", this.locY);
		config.set("Location.z", this.locZ);
		config.set("Level.money", this.levelmoney_cost_level);
		config.set("Level.rp", this.levelrp_cost_level);
		if (!config.isConfigurationSection("items")){
			ConfigurationSection items = config.createSection("items");
			IntStream.rangeClosed(1, LevelsKingdoms.getMax()).forEach(nbr -> {
				items.createSection("base" + String.valueOf(nbr));
				XItemStack.serialize(Empty_Item(nbr), config.getConfigurationSection("items.base" + String.valueOf(nbr)));
			});
			items.createSection("final");
			XItemStack.serialize(FinalEmpitItem(), config.getConfigurationSection("items.final"));
		}else{
			if (this.Final != null) XItemStack.serialize(this.Final, config.getConfigurationSection("items.final"));
			base.forEach((number, item) -> {
				if (item != null) XItemStack.serialize(item, config.getConfigurationSection("items.base" + String.valueOf(number)));
			});
			
		}

		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public int getLevelMoneyCost() {
		return levelmoney_cost_level;
	}

	public int getLevelRpCost() {
		return levelrp_cost_level;
	}

	public Double getX() {
		return locX;
	}

	public void setX(double x){
		this.locX = this.locX + x;
	}

	public Double getY() {
		return locY;
	}

	public void setY(double y){
		this.locY = this.locY  + y;
	}

	public Double getZ() {
		return locZ;
	}

	public void setZ(double z){
		this.locZ = this.locZ  + z;
	}

	public XMaterial getIcon() {
		return XMaterial.matchXMaterial(this.Final);
	}

	public Boolean CheckItem1(ItemStack item) {

		ItemStack checkItem = item;
		NamespacedKey key = new NamespacedKey(LevelsKingdoms.getInstance(), "Kingdoms-levels");
		ItemMeta itemMeta = checkItem.getItemMeta();
		PersistentDataContainer container = itemMeta.getPersistentDataContainer();

		if (container.has(key, PersistentDataType.STRING)) {
			String foundValue = container.get(key, PersistentDataType.STRING);
			LevelsKingdoms.logDebug("TEST: " + foundValue);
		}
		return false;
	}

	public Boolean CheckItem(ItemStack item) {

		return true;
	}

	public ItemStack getFinalItem() {
		return this.Final;
	}

	public void setFinalItem(ItemStack item){
		this.Final = item;
	}

	public ItemStack getBase(int baseNumber) {
		return base.get(baseNumber);
	}

	public void setBase(int number, ItemStack item){
		if (!this.base.containsKey(number)){
			this.base.put(number, item);
		}else{
			this.base.replace(number, item);
		}
	}

	public Collection<ItemStack> getBases() {
		return base.values();
	}

	public HashMap<Integer, ItemStack> getBasesHashMap(){
		return base;
	}

	public ItemStack Empty_Item(int number){
		ItemStack i1 = XMaterial.STICK.parseItem();
		ItemMeta i1m = i1.getItemMeta();
		Component title = Component.text("Base Item")
		                   .color(NamedTextColor.DARK_AQUA)
						   .decorate(TextDecoration.BOLD)
						   .append(Component.text(" Number: " + number, NamedTextColor.DARK_PURPLE));
		Component lore = Component.text("Click me to edit!")
		                   .color(NamedTextColor.GREEN);
		

		i1m.displayName(title);
		i1m.lore(Arrays.asList(lore));
		i1.setItemMeta(i1m);
		
		return i1;	
	}
		public ItemStack FinalEmpitItem(){
		ItemStack i1 = XMaterial.STICK.parseItem();
		ItemMeta i1m = i1.getItemMeta();
		Component title = Component.text("Final Item")
		                   .color(NamedTextColor.DARK_AQUA)
						   .decorate(TextDecoration.BOLD);
		Component lore = Component.text("Click me to edit!")
		                   .color(NamedTextColor.GREEN);
		

		i1m.displayName(title);
		i1m.lore(Arrays.asList(lore));
		i1.setItemMeta(i1m);
		
		return i1;	
	}
}
