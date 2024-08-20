package com.fuzion.party.manager.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.MenuInventory;
import com.fuzion.core.api.menu.MenuItem;
import com.fuzion.party.manager.classes.ClassManager;
import com.fuzion.party.manager.classes.ClassType;

public class ClassesMenu {
	
	private static int itemsPerPage = 21;
	
	public static void open(Player player) {
		int page = 1;
		MenuInventory menu = new MenuInventory("§0Escolha uma classe", 6, true);
		List<ClassType> classes = new ArrayList<>(ClassManager.getKits().values());
		Collections.sort(classes, new Comparator<ClassType>() {
			@Override
			public int compare(ClassType o1, ClassType o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		List<MenuItem> items = new ArrayList<>();
		
		for(int i = 0; i < 3; i++) {
			menu.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(1).name(" ").build());
		}
		for(int i = 3; i < 6; i++) {
			menu.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		}
		for(int i = 6; i < 9; i++) {
			menu.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(1).name(" ").build());
		}
		for(int i = 9; i < 18; i++) {
			menu.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		menu.setItem(18, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		menu.setItem(26, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		menu.setItem(27, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		menu.setItem(35, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		menu.setItem(36, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		menu.setItem(44, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		
		for (int i = 0; i < classes.size(); i++) {
			ClassType classe = classes.get(i);
			
			items.add(new MenuItem(new ItemBuilder().type(classe.getIcon()).name("§a" + classe.getName()).lore(classe.getDescription()).build()));
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
		
		
		for(int i = 51; i < 54; i++) {
			menu.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(5).name(" ").build());
		}
		
		for(int i = 48; i < 51; i++) {
			menu.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		}
		
		for(int i = 45; i < 48; i++) {
			menu.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(5).name(" ").build());
		}
		
		menu.open(player);
	}

}
