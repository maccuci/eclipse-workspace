package me.ale.pvp.manager.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.ale.pvp.api.menu.MenuInventory;
import me.ale.pvp.manager.ItemBuilder;

public class WarpsMenu {
	
	public void open(Player player) {
		MenuInventory menu = new MenuInventory("§8Warps", 1);
		ItemBuilder builder = new ItemBuilder();
		
		menu.setItem(0, builder.type(Material.STONE_SWORD).name("§cFFA").amount(Bukkit.getOnlinePlayers().size()).build());
		menu.setItem(4, builder.type(Material.MUSHROOM_SOUP).name("§3Training").amount(Bukkit.getOnlinePlayers().size()).build());
		menu.setItem(8, builder.type(Material.GRASS).name("§bClassic").amount(Bukkit.getOnlinePlayers().size()).build());
		
		menu.open(player);
	}
}
