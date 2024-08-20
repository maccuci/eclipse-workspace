package me.feenks.hg.game.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.feenks.hg.api.item.ItemBuilder;
import me.feenks.hg.api.menu.ClickType;
import me.feenks.hg.api.menu.MenuClickHandler;
import me.feenks.hg.api.menu.MenuInventory;
import me.feenks.hg.api.menu.MenuItem;
import me.feenks.hg.constructor.Kit;
import me.feenks.hg.manager.KitManager;

public class KitMenu {
	
	private static int itemsPerPage = 24;
	
	private static final ItemBuilder builder = new ItemBuilder();
	
	public static void open(Player player, int page) {
		MenuInventory menu = new MenuInventory("§eEscolha seu kit", 6);
		List<MenuItem> items = new ArrayList<>();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		Collections.sort(kits, new Comparator<Kit>() {

			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		
		for (int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			
			if(player.hasPermission("kit." + kit.getName())) {
				items.add(new MenuItem(builder.type(kit.getIconMaterial()).name("§a" + kit.getName()).lore(kit.getDescription()).build(), new MenuClickHandler() {
					
					@Override
					public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
						//PRE SELECT KIT
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
			menu.setItem(new ItemBuilder().type(Material.ITEM_FRAME).name("§cVocê não tem kit.").build(), 31);
		}
		
		menu.open(player);
	}
}
