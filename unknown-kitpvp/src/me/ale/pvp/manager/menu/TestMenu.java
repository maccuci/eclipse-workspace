package me.ale.pvp.manager.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.pvp.Main;
import me.ale.pvp.api.menu.MenuInventory;
import me.ale.pvp.manager.ItemBuilder;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitManager;

public class TestMenu {
	
	List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
	ItemBuilder builder = new ItemBuilder();
	
	public void open(Player player) {
		new BukkitRunnable() {
			int rotation = 0;
			
			@Override
			public void run() {
				rotation++;
				
				if(rotation <= 20) {
					MenuInventory menu = new MenuInventory("§3Test", 3);
					
					for (int i = 0; i < 27; i++) {
						menu.setItem(22, builder.type(Material.STAINED_GLASS_PANE).durability(14).build());
						menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).durability(7).build());
					}
					
					for (int i = 0; i < 18; i++) {
						Kit kit = kits.get(new Random().nextInt(kits.size()));

						menu.setItem(i,
								builder.type(kit.getIcon()).name("§9" + kit.getName()).lore(kit.getLore()).build());
					}
					for (int i = 0; i < 9; i++) {
						menu.setItem(4, builder.type(Material.STAINED_GLASS_PANE).durability(14).build());
						menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).durability(7).build());
					}
					
					player.playSound(player.getLocation(), Sound.CLICK, 0.5f, 0.5f);
					menu.open(player);
				}
				
				if(rotation == 21) {
					MenuInventory menu = new MenuInventory("§3Test", 3);
					Kit kit = kits.get(new Random().nextInt(kits.size()));
					
					for (int i = 0; i < 9; i++) {
						menu.setItem(4, builder.type(Material.STAINED_GLASS_PANE).durability(14).build());
						menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).durability(7).build());
					}
					
					for (int i = 0; i < 27; i++) {
						menu.setItem(22, builder.type(Material.STAINED_GLASS_PANE).durability(14).build());
						menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).durability(7).build());
					}
					
					for (int i = 0; i < 18; i++) {

						menu.setItem(i,
								builder.type(kit.getIcon()).name("§9" + kit.getName()).lore(kit.getLore()).build());
					}
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 7.0f, 7.0f);
					Bukkit.broadcastMessage("§aTESTE " + kit.getName());
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0L, 5L);
	}

}
