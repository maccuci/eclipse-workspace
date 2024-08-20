package me.ale.pvp.manager.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.ale.pvp.api.menu.MenuInventory;
import me.ale.pvp.manager.ItemBuilder;

public class FactionMenu {
	
	ItemBuilder builder = new ItemBuilder();
	
	public void open(Player player) {
		MenuInventory menu = new MenuInventory("§8Selecione a sua faction", 3);
		
		menu.setItem(11, builder.type(Material.NETHER_STAR).name("§2Ninja Faction").lore("§7Clique para entrar nessa faction.").build());
		menu.setItem(13, builder.type(Material.IRON_SWORD).name("§dSamurai Faction").lore("§7Clique para entrar nessa faction.").build());
		menu.setItem(15, builder.type(Material.REDSTONE).name("§4Shogun Faction").lore("§7Clique para entrar nessa faction.").build());
		
		menu.open(player);
	}

}
