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

import me.ale.pvp.api.menu.ClickType;
import me.ale.pvp.api.menu.MenuClickHandler;
import me.ale.pvp.api.menu.MenuInventory;
import me.ale.pvp.api.menu.MenuItem;
import me.ale.pvp.manager.ItemBuilder;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitManager;

public class KitMenu {
	
	private static int itemsPerPage = 21;
	
	ItemBuilder builder = new ItemBuilder();
	
	public void open(Player player, int page) {
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		Collections.sort(kits, new Comparator<Kit>() {
			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		
		MenuInventory menu = new MenuInventory("§8Seus Kits", 6);
		
		menu.setItem(3, builder.type(Material.CHEST).name("§aSeus Kits").build());
		menu.setItem(5, new MenuItem(builder.type(Material.ENDER_CHEST).name("§7Todos os Kits").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				allkits(p, 1);
			}
		}));
		
		List<MenuItem> items = new ArrayList<>();
		for (int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			
			items.add(new MenuItem(builder.type(kit.getIcon()).name("§9" + kit.getName()).lore(kit.getLore()).build()));
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
					open(arg0, page - 1);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Posterior").lore(Arrays.asList("§7Clique para ir para a página", "§7posterior")).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					open(arg0, page + 1);
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
		
		menu.open(player);
	}
	
	public void allkits(Player player, int page) {
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		Collections.sort(kits, new Comparator<Kit>() {
			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		
		MenuInventory menu = new MenuInventory("§8Todos os Kits", 6);
		
		menu.setItem(3, new MenuItem(builder.type(Material.CHEST).name("§7Seus Kits").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				open(p, 1);
			}
		}));
		menu.setItem(5, builder.type(Material.ENDER_CHEST).name("§aTodos os Kits").build());
		
		List<MenuItem> items = new ArrayList<>();
		for (int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			
			items.add(new MenuItem(builder.type(kit.getIcon()).name("§9" + kit.getName()).lore(kit.getLore()).build()));
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
					allkits(arg0, page - 1);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Posterior").lore(Arrays.asList("§7Clique para ir para a página", "§7posterior")).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					allkits(arg0, page + 1);
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
		
		menu.open(player);
	}
}
