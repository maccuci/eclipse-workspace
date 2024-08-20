package com.fuzion.hg.manager.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.ClickType;
import com.fuzion.core.api.menu.MenuClickHandler;
import com.fuzion.core.api.menu.MenuInventory;
import com.fuzion.core.api.menu.MenuItem;
import com.fuzion.core.master.account.management.KitFavoritesManager;
import com.fuzion.hg.event.PlayerSelectKitEvent;
import com.fuzion.hg.manager.kit.Kit;
import com.fuzion.hg.manager.kit.KitManager;

public class KitMenu implements Listener {
	
	private static int itemsPerPage = 21;
	
	public static void open(Player player, int page) {
		MenuInventory menu = new MenuInventory("§0Selecione seu kit", 6, true);
		
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		Collections.sort(kits, new Comparator<Kit>() {
			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		List<MenuItem> items = new ArrayList<>();
		
		for (int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);

			if(kit.canUseKit(player)) {
				items.add(new MenuItem(new ItemBuilder().type(kit.getIcon()).name("§a" + kit.getName()).lore(kit.getExplanation()).durability(kit.getDurability()).build(), new SelectKit(kit)));
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
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.WOOD_PLATE).name("§cPágina Anterior").lore("§7Clique para voltar uma página.").build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					open(arg0, page - 1);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.STONE_PLATE).name("§aPágina Posterior").lore("§7Clique para carregar uma nova página.").build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					open(arg0, page + 1);
				}
			}), 35);
		} else {
			menu.setItem(new ItemBuilder().type(Material.STONE_PLATE).name("§f§oNão há próxima página.").build(), 35);
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
	
	public static class SelectKit implements MenuClickHandler {
		private Kit kit;
		
		public SelectKit(Kit kit) {
			this.kit = kit;
		}
		
		@Override
		public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
			if(arg2 == ClickType.LEFT) {
				if(!kit.canUseKit(arg0)) {
					return;
				}
				PlayerSelectKitEvent event = new PlayerSelectKitEvent(arg0, kit);
				Bukkit.getPluginManager().callEvent(event);
				arg0.closeInventory();
			} else if(arg2 == ClickType.RIGHT) {
				if(KitFavoritesManager.kitsFavoritos.contains(kit.getName())) {
					arg0.sendMessage("§cVocê já possui este kit como seu favorito.");
					return;
				}
				new KitFavoritesManager().addKitFavorite(arg0, kit.getName());
				open(arg0, 1);
				arg0.sendMessage("§eVocê adicionou o kit " + kit.getName() + " como seu favorito.");
			}
			return;
		}
	}
}
