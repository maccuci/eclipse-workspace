package me.tony.pvp.manager.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.tony.commons.api.item.ItemBuilder;
import me.tony.commons.api.menu.ClickType;
import me.tony.commons.api.menu.MenuClickHandler;
import me.tony.commons.api.menu.MenuInventory;
import me.tony.commons.api.menu.MenuItem;
import me.tony.pvp.manager.kit.Kit;
import me.tony.pvp.manager.kit.KitManager;

public class KitMenu {
	
	static int itemsPerPage = 36;
	
	public static void open(Player player, int page) {
		MenuInventory menu = new MenuInventory("§eSeus Kits §7[§81§7]", 6, true);
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		List<MenuItem> items = new ArrayList<>();
		Collections.sort(kits, new Comparator<Kit>() {

			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			
			items.add(new MenuItem(new ItemBuilder().type(kit.getIcon()).name("§f§l» §7Kit §f" + kit.getName()).lore(kit.getLore()).build()));
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
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.STAINED_GLASS_PANE).name("§aPágina Anterior").lore("§7" + page + " / " + pageEnd).durability(14).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					open(arg0, page - 1);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.STAINED_GLASS_PANE).name("§aPágina Posterior").lore("§7" + page + " / " + pageEnd).durability(5).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					open(arg0, page + 1);
				}
			}), 35);
		} else {
			menu.setItem(new ItemBuilder().type(Material.STAINED_GLASS_PANE).name("§f§oNão há próxima página.").lore("§7" + page + " / " + pageEnd).durability(14).build(), 35);
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
		
		menu.open(player);
	}
	
	public static void debug(Player player, int page) {
		MenuInventory menu = new MenuInventory("§eSeus Kits §7[§81§7]", 6, true);
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		List<MenuItem> items = new ArrayList<>();
		Collections.sort(kits, new Comparator<Kit>() {

			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			
			for(int j = 0; j < 18; j++) {
				menu.setItem(j, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
				menu.setItem(0, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(11).name(" ").build());
				menu.setItem(1, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(11).name(" ").build());
				menu.setItem(2, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
				menu.setItem(3, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
				menu.setItem(4, new ItemBuilder().type(Material.SLIME_BALL).name("§aSeus Kits").lore("§fEm breve mais opções.").build());
				menu.setItem(6, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
				menu.setItem(5, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
				menu.setItem(7, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(11).name(" ").build());
				menu.setItem(8, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(11).name(" ").build());
			}
			items.add(new MenuItem(new ItemBuilder().type(kit.getIcon()).name("§f§l» §7Kit §f" + kit.getName()).lore(kit.getLore()).build()));
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
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.CARPET).name("§cPágina Anterior").lore("§7" + page + " / " + pageEnd).durability(14).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					open(arg0, page - 1);
				}
			}), 0);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.CARPET).name("§aPágina Posterior").durability(5).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					open(arg0, page + 1);
				}
			}), 8);
		} else {
			menu.setItem(new ItemBuilder().type(Material.CARPET).name("§cNão há próxima página.").durability(14).build(), 8);
		}
		int w = 18;

		for (int i = pageStart; i < pageEnd; i = i + 1) {
			MenuItem item = items.get(i);
			menu.setItem(item, w);
			w += 1;
		}
		
		menu.open(player);
	}

}
