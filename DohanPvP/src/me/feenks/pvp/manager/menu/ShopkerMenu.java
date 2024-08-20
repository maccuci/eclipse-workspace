package me.feenks.pvp.manager.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.feenks.core.bukkit.api.item.ItemBuilder;
import me.feenks.core.bukkit.api.menu.ClickType;
import me.feenks.core.bukkit.api.menu.MenuClickHandler;
import me.feenks.core.bukkit.api.menu.MenuInventory;
import me.feenks.core.bukkit.api.menu.MenuItem;

public class ShopkerMenu {
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§bVendedor", 3, true);
		
		for(int i = 0; i < 9; i++) {
			menu.setItem(new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(7).name(" ").build(), i);
		}
		
		for(int i = 9; i < 18; i++) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.DIAMOND_SWORD).name("§aComprar habilidades").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					AbilitiesMenu.open(arg0, 1);
				}
			}), 12);
			menu.setItem(new ItemBuilder().type(Material.GOLD_INGOT).glow().name("§aComprar prestígios").build(), 14);
			menu.setItem(new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build(), i);
		}
		
		for(int i = 18; i < 27; i++) {
			menu.setItem(new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(7).name(" ").build(), i);
		}
		
		menu.open(player);
	}
}
