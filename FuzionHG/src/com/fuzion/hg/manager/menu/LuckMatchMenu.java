package com.fuzion.hg.manager.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.MenuInventory;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.kit.Kit;
import com.fuzion.hg.manager.kit.KitManager;
import com.fuzion.hg.manager.kit.KitMatch;

public class LuckMatchMenu {
	
	static HashMap<String, BukkitRunnable> task = new HashMap<>();
	public static HashMap<String, Integer> speed = new HashMap<>();
	static HashMap<String, Integer> time = new HashMap<>();
	public static HashMap<Player, String> kitMatch = new HashMap<>();
	
	public static void open(Player player) {
		if(speed.containsKey(player.getName()))
			return;
		MenuInventory menu = new MenuInventory("§6Surpresa da partida", 3);
		for(int i = 0; i < 27; i++) {
			menu.setItem(new ItemBuilder().type(Material.STAINED_GLASS_PANE).name(" ").durability(7).build(), i);
		}
		speed.put(player.getName(), Integer.valueOf(0));
	    time.put(player.getName(), Integer.valueOf(0));
	    if(!task.containsKey(player.getName())) {
	    	try {
				task.put(player.getName(), (BukkitRunnable) new BukkitRunnable() {
					
					@Override
					public void run() {
						speed.put(player.getName(), Integer.valueOf(((Integer)speed.get(player.getName())).intValue() + 1));
			              if (((Integer)speed.get(player.getName())).intValue() % 20 == 0) {
			                time.put(player.getName(), Integer.valueOf(((Integer)time.get(player.getName())).intValue() + 1));
			              }
			              if (((Integer)speed.get(player.getName())).intValue() < 50) {
			            	  loop(menu);
			            	  player.playSound(player.getLocation(), Sound.NOTE_BASS, 8F, 8F);
				              return;
			              }
			              if (((Integer)speed.get(player.getName())).intValue() < 100) {
			            	  if (((Integer)speed.get(player.getName())).intValue() % 3 == 0) {
			            		  loop(menu);
				            	  player.playSound(player.getLocation(), Sound.NOTE_BASS, 8F, 8F);
			            	  }
			            	  return;
			              }
			              if (((Integer)speed.get(player.getName())).intValue() < 160) {
			            	  if (((Integer)speed.get(player.getName())).intValue() % 6 == 0) {
			            		  loop(menu);
				            	  player.playSound(player.getLocation(), Sound.NOTE_BASS, 8F, 8F);
			            	  }
			            	  return;
			              }
			              if (((Integer)speed.get(player.getName())).intValue() < 240) {
			            	  if (((Integer)speed.get(player.getName())).intValue() % 10 == 0) {
			            		  loop(menu);
				            	  player.playSound(player.getLocation(), Sound.NOTE_BASS, 8F, 8F);
			            	  }
			            	  return;
			              }
			              if (((Integer)speed.get(player.getName())).intValue() > 240) {
			            	  player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
			            	  String kitName = menu.getItem(13).getStack().getItemMeta().getDisplayName().replace("§a", "");
			            	  player.sendMessage("§aO seu kit surpresa da partida foi §l" + kitName.toUpperCase());
			            	  kitMatch.put(player, kitName);
			            	  KitMatch.setKitMatch(player, kitMatch.get(player));
			  				  System.out.println(KitMatch.getKitMatch(player));
			            	  
				              new BukkitRunnable() {
									
									@Override
									public void run() {
										player.closeInventory();
									}
								}.runTaskLater(Main.getPlugin(), 20);
								cancel();
			              }
					}
				}.runTaskTimer(Main.getPlugin(), 0, 1));
			} catch (Exception e) {}
	    }
		menu.open(player);
	}
	
	public static void loop(MenuInventory menu) {
		menu.setItem(4, new ItemBuilder().type(Material.HOPPER).name("§e§lSua surpresa é §f" + menu.getItem(13).getStack().getItemMeta().getDisplayName()).build());
	    
		menu.setItem(menu.getItem(11), 10);
		menu.setItem(menu.getItem(12), 11);
		menu.setItem(menu.getItem(13), 12);
		menu.setItem(menu.getItem(14), 13);
		menu.setItem(menu.getItem(15), 14);
		menu.setItem(menu.getItem(16), 15);
		
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		int ii = new Random().nextInt(kits.size());
    	Kit kit = kits.get(ii);
    	menu.setItem(16, new ItemBuilder().type(kit.getIcon()).name("§a" + kit.getName()).lore(kit.getExplanation()).durability(kit.getDurability()).build());
	}
}
