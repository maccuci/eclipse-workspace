package me.ale.pvp.manager.feast;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.commons.api.actionbar.ActionBarAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.pvp.Main;

public class FeastManager {
	
	static Chest[] chests = new Chest[12];
    static ArrayList<Location> fblocks = new ArrayList<>();
	static Integer time = Integer.valueOf(15);
	
	public void spawn() {
		new BukkitRunnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if(time > 0) {
					time = Integer.valueOf(time.intValue() - 1);
					ActionBarAPI.broadcast("O feast vai spawnar em §a" + ItemBuilder.format(time.intValue()) + " segundo" + (time.intValue() > 1 ? "s" : ""));
					
					if(time == 0) {
						ActionBarAPI.broadcast("O feast foi spawnado!");
						ArrayList<ItemStack> item = new ArrayList<>();
						
						List<String> list = Main.getPlugin().getConfig().getStringList("ITEMS");
						Integer id;
						
						for(int x = 0; x < list.size(); x++) {
							String[] split = ((String)list.get(x)).split(",");
							
							if (!split[0].contains(":")) {
								if (split.length == 2) {
									id = Integer.valueOf(split[0]);
					                Integer amount = Integer.valueOf(split[1]);
					                
					                item.add(new ItemBuilder().type(Material.getMaterial(id)).amount(amount).build());
								}
							} else {
								String[] split2 = split[0].split(":");
								id = Integer.valueOf(split2[0]);
				                Integer amount = Integer.valueOf(split[1]);
				                Integer data = Integer.valueOf(split2[1]);
				                
				                item.add(new ItemBuilder().type(Material.getMaterial(id)).durability(data).amount(amount).build());
							}
						}
					}
				}
			}
		};
	}
}
