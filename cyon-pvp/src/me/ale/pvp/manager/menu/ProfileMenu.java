package me.ale.pvp.manager.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.api.menu.ClickType;
import me.ale.commons.api.menu.MenuClickHandler;
import me.ale.commons.api.menu.MenuInventory;
import me.ale.commons.api.menu.MenuItem;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.manager.StatsManager;
import me.ale.pvp.Main;
import me.ale.pvp.manager.achievement.AchievementManager;
import me.ale.pvp.manager.achievement.type.AchievementType;

public class ProfileMenu {
	
	static final ItemBuilder builder = new ItemBuilder();
	private static int itemsPerPage = 21;
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§8Perfil", 3);
		
		for(int i = 0; i < 27; i++) {
			menu.setItem(10, new MenuItem(builder.type(Material.BOOK).name("§aStatus").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					status(arg0);
				}
			}));
			menu.setItem(12, builder.type(Material.FIREWORK_CHARGE).name("§cConfigurações").build());
			menu.setItem(14, builder.type(Material.NOTE_BLOCK).name("§7[§cNone§7]").build());
			menu.setItem(16, new MenuItem(builder.type(Material.DIAMOND).name("§3Conquistas").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					achievements(arg0, 1);
				}
			}));
			menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(7).build());
		}
		
		menu.open(player);
	}
	
	public static void status(Player player) {
		MenuInventory menu = new MenuInventory("§aStatus", 3);
		StatsManager manager = BukkitMain.getPlugin().getStatsManager();
		
		for(int i = 0; i < 27; i++) {
			menu.setItem(10, builder.type(Material.DIAMOND_SWORD).name("§7Kills §8> §a" + manager.get(player.getUniqueId(), "kills")).build());
			menu.setItem(12, builder.type(Material.SKULL_ITEM).durability(3).skin(player.getName()).name("§7Deaths §8> §c" + manager.get(player.getUniqueId(), "deaths")).build());
			menu.setItem(14, builder.type(Material.PAPER).name("§7Money §8> §2" + manager.get(player.getUniqueId(), "money")).build());
			menu.setItem(16, builder.type(Material.GOLD_INGOT).name("§7Exp §8> §e" + manager.get(player.getUniqueId(), "exp")).build());
			menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(7).build());
		}
		
		menu.open(player);
	}
	
	public static void achievements(Player player, int page) {
		MenuInventory menu = new MenuInventory("§3Conquistas", 6);
		List<AchievementType> types = new ArrayList<>();
		Collections.sort(types, new Comparator<AchievementType>() {
			@Override
			public int compare(AchievementType o1, AchievementType o2) {
				return o1.getItemName().compareTo(o2.getItemName());
			}

		});
		List<MenuItem> items = new ArrayList<>();
		
		for(AchievementType type : AchievementType.values()) {
			types.add(type);
		}
		
		for(int i = 0; i < types.size(); i++) {
			AchievementType achievement = types.get(i);
			AchievementManager manager = Main.getPlugin().getAchievementManager();
			
			if(manager.hasAchievement(player, achievement)) {
					items.add(new MenuItem(builder.type(Material.DIAMOND_BLOCK).name("§a" + achievement.getItemName()).lore(achievement.getDesc()).lore(" ", "§eRecompensa: §b" + achievement.getReward() + " Coins.").build()));
				} else {
					items.add(new MenuItem(builder.type(Material.COAL_BLOCK).name("§c" + achievement.getItemName()).lore(achievement.getDesc()).lore(" ", "§eRecompensa: §b" + achievement.getReward() + " Coins.").build()));
			}
		}
		int pageStart = 0;
		int pageEnd = itemsPerPage;
		if (page > 1) {
			pageStart = ((page - 1) * itemsPerPage);
			pageEnd = (page * itemsPerPage);
		}
		if (pageEnd > items.size()) {
			pageEnd = items.size();
		}
		if (page == 1) {
			
		} else {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Anterior").lore(Arrays.asList("§7Clique para ir para a página", "§7anterior")).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					achievements(arg0, page - 1);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Posterior").lore(Arrays.asList("§7Clique para ir para a página", "§7posterior")).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					achievements(arg0, page + 1);
				}
			}), 35);
		} else {
			menu.setItem(new ItemBuilder().type(Material.INK_SACK).durability(8).name("§cPágina Posterior").build(), 35);
		}
		
		int w = 19;

		for (int i = pageStart; i < pageEnd; i++) {
			MenuItem item = items.get(i);
			menu.setItem(item, w);
			if (w % 9 == 7) {
				w += 3;
				continue;
			}
			w += 1;
		}
		
		if(items.size() == 0) {
			menu.setItem(31, builder.type(Material.ITEM_FRAME).name("§cNenhuma conquista encontrada.").build());
		}
		
		menu.open(player);
	}
}
