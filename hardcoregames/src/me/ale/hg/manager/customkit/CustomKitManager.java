package me.ale.hg.manager.customkit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.Main;

public class CustomKitManager {
	
	private File file = new File(Main.getPlugin().getDataFolder() + "/customkits.yml");
	private FileConfiguration files = YamlConfiguration.loadConfiguration(this.file);
	
	public void create() {
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			files.save(file);
		} catch (Exception e) {}
	}
	
	public void addAbility(Player player, String ability) {
		String s = files.getString("CustomKit." + player.getName()) != null ? files.getString("CustomKit." + player.getName()) : "";
		files.set("CustomKit." + player.getName(), s + "," + ability);
	    save();
	}
	
	public void removeAbility(Player player, String ability) {
		String s = files.getString("CustomKit." + player.getName()) != null ? files.getString("CustomKit." + player.getName()) : "";
	    if (s.contains(ability)) {
	        s.replace("," + ability, "");
	      }
	      files.set("CustomKit." + player.getName(), s);
	      save();
	}
	
	public void changeItem(Player player, ItemStack item) {
		files.set("CustomKit." + player.getName() + ".Item", item.getType().name());
		save();
	}
	
	public void changeCustomName(Player player, String name) {
		files.set("CustomKit." + player.getName() + ".Name", name);
		save();
	}
	
	public void setPoints(Player player, int points) {
		if(points > 25) {
			return;
		}
		files.set("CustomKit." + player.getName() + ".Points", getPoints(player) + points);
		save();
	}
	
	public int getPoints(Player player) {
		return files.getInt("CustomKit." + player.getName() + ".Points");
	}
	
	public String getItemName(Player player) {
		return files.getString("CustomKit." + player.getName() + ".Item") != null ? files.getString("CustomKit." + player.getName() + ".Item") : /*KitManager.getKit(getKitName(player)).getName()*/ "STONE";
	}
	
	public Material getItemType(Player player) {
		return getItemName(player) != null ? Material.valueOf(getItemName(player)) : Material.AIR;
	}
	
	public String getKitName(Player player) {
		return files.getString("CustomKit." + player.getName() + ".Name") != null ? files.getString("CustomKit." + player.getName() + ".Name") : "Customkit#1";
	}
	
	public String getKitAbilities(Player player) {
		return files.getString("CustomKit." + player.getName() + ".Abilities");
	}
}
