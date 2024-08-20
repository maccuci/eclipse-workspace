package com.fuzion.kitpvp.manager.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.ClickType;
import com.fuzion.core.api.menu.MenuClickHandler;
import com.fuzion.core.api.menu.MenuInventory;
import com.fuzion.core.api.menu.MenuItem;
import com.fuzion.kitpvp.event.PlayerWarpJoinEvent;

public class WarpMenu implements Listener {
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§8Arenas de treino", 3, true);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.GLASS).name("§aArena FPS").lore("§7Treine em uma arena mais leve.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				player.performCommand("warp fps");
			}
		}), 11);
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.LAVA_BUCKET).name("§aArena Lava").lore("§7Treine em corredores de lava.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				player.performCommand("warp challenge");
			}
		}), 12);
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.BLAZE_ROD).name("§aArena 1v1").lore("§7Desafie jogadores para uma batalha.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				PlayerWarpJoinEvent joinEvent = new PlayerWarpJoinEvent(player, "1v1");
				Bukkit.getPluginManager().callEvent(joinEvent);
				player.closeInventory();
			}
		}), 13);
		
		menu.open(player);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		Player player = (Player)event.getWhoClicked();
		if (event.getInventory() == null)
			return;
		if (inv.getType() != InventoryType.CHEST)
			return;
		if (inv.getHolder() == null)
			return;
		event.setCancelled(true);
		if (event.getClickedInventory() != inv)
			return;
		if (!(event.getWhoClicked() instanceof Player))
			return;
		if (event.getSlot() < 0)
			return;
		event.setCancelled(true);
		
		if(event.getCurrentItem().getItemMeta() == null)
			return;
		if(event.getCurrentItem().getItemMeta().getDisplayName() == null)
			return;
		if(event.getCurrentItem().getType() == Material.AIR)
			return;
		
		if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§aFps")) {
			player.performCommand("warp fps");
			player.closeInventory();
		}
		if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§aChallenge")) {
			player.performCommand("warp challenge");
			player.closeInventory();
		}
		if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§a1v1")) {
			PlayerWarpJoinEvent joinEvent = new PlayerWarpJoinEvent(player, "1v1");
			Bukkit.getPluginManager().callEvent(joinEvent);
			player.closeInventory();
		}
	}
}
