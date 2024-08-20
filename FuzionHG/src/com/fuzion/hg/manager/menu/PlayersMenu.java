package com.fuzion.hg.manager.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.ClickType;
import com.fuzion.core.api.menu.MenuClickHandler;
import com.fuzion.core.api.menu.MenuInventory;
import com.fuzion.core.api.menu.MenuItem;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.KitManager;

public class PlayersMenu {
	
	private static int itemsPerPage = 36;
	
	public static void open(Player player, List<Player> players, int page) {
		MenuInventory menu = new MenuInventory("§0Jogadores vivos", 6, true);
		Collections.sort(players, new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		List<MenuItem> items = new ArrayList<>();
		
		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			
			items.add(new MenuItem(new ItemBuilder().skin(p.getName()).type(Material.SKULL_ITEM).durability(3).name("§a" + p.getName()).lore("§fKills: §e" + GameManager.getKills(p), "§fKit: §a" + new KitManager().getKitName(p), " ", "§bClique para teleportar.").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					arg0.teleport(p);
					arg0.sendMessage("§eVocê foi até o jogador " + p.getName());
				}
			}));
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
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.WOOD_PLATE).name("§aPágina Anterior").lore("§7Clique para atualizar para a página §a" + (page - 1)).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					open(arg0, players, page - 1);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.STONE_PLATE).name("§aPágina Posterior").lore("§7Clique para atualizar para a página §a" + (page + 1)).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					open(arg0, players, page + 1);
				}
			}), 35);
		} else {
			menu.setItem(new ItemBuilder().type(Material.STONE_PLATE).name("§cPágina Posterior").build(), 35);
		}
		int w = 9;
		
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
			menu.setItem(new ItemBuilder().type(Material.ITEM_FRAME).build(), 25);
		}
		
		menu.open(player);
	}

}
