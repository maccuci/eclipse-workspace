package me.feenks.pvp.manager.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.feenks.core.bukkit.api.item.ItemBuilder;
import me.feenks.core.bukkit.api.menu.ClickType;
import me.feenks.core.bukkit.api.menu.MenuClickHandler;
import me.feenks.core.bukkit.api.menu.MenuInventory;
import me.feenks.core.bukkit.api.menu.MenuItem;
import me.feenks.pvp.events.PlayerSelectAbilityEvent;
import me.feenks.pvp.manager.ability.Ability;
import me.feenks.pvp.manager.ability.AbilityManager;

public class AbilitiesMenu {
	
	static int itemsPerPage = 21;
	
	public static void open(Player player, int page) {
		MenuInventory menu = new MenuInventory("§aSuas habilidades", 6, true);
		List<Ability> abilities = new ArrayList<>(AbilityManager.getAbilities().values());
		Collections.sort(abilities, new Comparator<Ability>() {
			@Override
			public int compare(Ability o1, Ability o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		List<MenuItem> items = new ArrayList<>();
		
		for(int i = 0; i < abilities.size(); i++) {
			Ability ability = abilities.get(i);
			
			items.add(new MenuItem(new ItemBuilder().type(ability.getIcon()).name("§a" + ability.getName()).lore(ability.getDescription()).build(), new SelectAbility(ability)));
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
	
	public static class SelectAbility implements MenuClickHandler {

		Ability ability;
		
		public SelectAbility(Ability ability) {
			this.ability = ability;
		}
		
		@Override
		public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
			PlayerSelectAbilityEvent event = new PlayerSelectAbilityEvent(arg0, ability);
			Bukkit.getPluginManager().callEvent(event);
			arg0.closeInventory();
		}		
	}
}
