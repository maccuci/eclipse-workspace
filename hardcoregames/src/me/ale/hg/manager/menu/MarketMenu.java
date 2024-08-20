package me.ale.hg.manager.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.api.item.ItemBuilder;
import me.ale.hg.api.menu.ClickType;
import me.ale.hg.api.menu.MenuClickHandler;
import me.ale.hg.api.menu.MenuInventory;
import me.ale.hg.api.menu.MenuItem;
import me.ale.hg.manager.kit.Kit;
import me.ale.hg.manager.kit.KitManager;

public class MarketMenu {
	
	private static final ItemBuilder builder = new ItemBuilder();
	private static int itemsPerPage = 21;
	
	public static void open(Player player, int page) {
		MenuInventory menu = new MenuInventory("§0Loja de kits", 6);
		List<MenuItem> items = new ArrayList<>();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		Collections.sort(kits, new Comparator<Kit>() {

			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		menu.setItem(3, new MenuItem(builder.type(Material.WOOL).name("§fSeus Kits").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				KitMenu.open(p, page);
			}
		}));
		menu.setItem(4, new MenuItem(builder.type(Material.WOOL).name("§fTodos os Kits").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				KitMenu.all(p, page);
			}
		}));
		menu.setItem(5, new MenuItem(builder.type(Material.WOOL).name("§aLoja de kits").durability(5).build()));
		
		for (int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			
			if(player.hasPermission("kit." + kit.getName())) {
				items.add(new MenuItem(builder.type(kit.getIcon()).name("§a" + kit.getName()).lore(kit.getLore()).lore(" ", "§cVocê já possui.").build()));
			} else {
				items.add(new MenuItem(builder.type(kit.getIcon()).name("§c" + kit.getName()).lore(kit.getLore()).lore(" ", "§7Compre por: §610.000").build(), new MenuClickHandler() {
					
					@Override
					public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
						p.closeInventory();
						p.sendMessage("§cVocê não possui coins suficientes para comprar este kit.");
						return;
					}
				}));
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
			menu.setItem(0, new MenuItem(builder.type(Material.CARPET).durability(5).name("§aPágina anterior").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
					open(p, page - 1);
				}
			}));
		}
		
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(8, new MenuItem(builder.type(Material.CARPET).durability(5).name("§aPágina posterior").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
					open(p, page + 1);
				}
			}));
		} else {
			menu.setItem(8, builder.type(Material.CARPET).durability(14).name("§cPágina posterior").build());
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
		
		if (items.size() == 0) {
			menu.setItem(new ItemBuilder().type(Material.ITEM_FRAME).name("§cNão tem kits a venda.").build(), 31);
		}
		
		menu.open(player);
	}
}
