package me.ale.pvp.manager.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.api.menu.ClickType;
import me.ale.commons.api.menu.MenuClickHandler;
import me.ale.commons.api.menu.MenuInventory;
import me.ale.commons.api.menu.MenuItem;
import me.ale.pvp.event.WarpSelectEvent;
import me.ale.pvp.manager.warp.Warp;

public class PvPWarpsMenu {
	
	static final ItemBuilder builder = new ItemBuilder();
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§2PvP Warps", 1);
		
		menu.setItem(0, new MenuItem(builder.type(Material.STONE_SWORD).name("§3FFA").lore("§7Jogadores na arena: §f0").amount(Bukkit.getOnlinePlayers().size()).build(), new SelectWarp(Warp.FFA)));
		menu.setItem(2, new MenuItem(builder.type(Material.LAVA_BUCKET).name("§6Challenges").lore("§7Jogadores na arena: §f0").amount(Bukkit.getOnlinePlayers().size()).build(), new SelectWarp(Warp.CHALLENGES)));
		menu.setItem(4, new MenuItem(builder.type(Material.BLAZE_ROD).name("§eDesafios Solos").lore("§7Jogadores na arena: §f0").amount(Bukkit.getOnlinePlayers().size()).build(), new SelectWarp(Warp.CHALLENGES_SOLO)));
		menu.setItem(6, new MenuItem(builder.type(Material.SIGN).name("§cMercado").lore("§7Jogadores na arena: §f0").amount(Bukkit.getOnlinePlayers().size()).build(), new SelectWarp(Warp.MARKET)));
		menu.setItem(8, new MenuItem(builder.type(Material.GRASS).name("§aClassica").lore("§7Jogadores na arena: §f0").amount(Bukkit.getOnlinePlayers().size()).build(), new SelectWarp(Warp.FFA)));
		
		menu.open(player);
	}
	
	public static class SelectWarp implements MenuClickHandler {
		
		private Warp warp;
		
		public SelectWarp(Warp warp) {
			this.warp = warp;
		}
		
		@Override
		public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
			WarpSelectEvent event = new WarpSelectEvent(arg0, warp);
			Bukkit.getPluginManager().callEvent(event);
			arg0.closeInventory();
			return;
		}
		
	}
}
