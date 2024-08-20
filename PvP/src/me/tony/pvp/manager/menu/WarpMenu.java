package me.tony.pvp.manager.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.tony.commons.api.item.ItemBuilder;
import me.tony.commons.api.menu.ClickType;
import me.tony.commons.api.menu.MenuClickHandler;
import me.tony.commons.api.menu.MenuInventory;
import me.tony.commons.api.menu.MenuItem;

public class WarpMenu {
	
	public static void warpsList(Player player) {
		MenuInventory menu = new MenuInventory("§eWarps", 1, true);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.DIAMOND_SWORD).name("§cCombate").lore("§7Aqui está apenas a warps de quem", "§7deseja algum tipo de combate!").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				combat(player);
			}
		}), 3);
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.NAME_TAG).name("§aMiniGames").lore("§7Está afim de jogar algum minigame?").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				minigames(player);
			}
		}), 4);
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.LAVA_BUCKET).name("§6Desafios").lore("§7Será que você é forte o suficiente", "§7para completar algum desafio?").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				challenges(arg0);
			}
		}), 5);
		
		menu.open(player);
	}
	
	public static void combat(Player player) {
		MenuInventory menu = new MenuInventory("§eWarps - Combate", 3, true);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		menu.setItem(11, itemBuilder.type(Material.GLASS).name("§eFps").lore("§7Clique para entrar.").build());
		menu.setItem(12, itemBuilder.type(Material.BLAZE_ROD).name("§61v1").lore("§7Clique para entrar.").build());
		menu.setItem(13, itemBuilder.type(Material.DIAMOND_SWORD).name("§cMain").lore("§7Clique para entrar.").build());
		menu.setItem(15, itemBuilder.type(Material.CAKE).name("§dParty §ePvP").glow().lore("§7Calma, a festa ainda não teve o seu início.", " ", "§7Em breve.").build());
		
		menu.open(player);
	}
	
	public static void challenges(Player player) {
		MenuInventory menu = new MenuInventory("§eWarps - Desafios", 3, true);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		menu.setItem(11, itemBuilder.type(Material.TRIPWIRE_HOOK).name("§3Drops aleatórios").lore("§7Clique para entrar.").build());
		menu.setItem(12, itemBuilder.type(Material.LAVA_BUCKET).name("§6Lava").lore("§7Clique para entrar.").build());
		menu.setItem(13, itemBuilder.type(Material.WATER_BUCKET).name("§9Mlg").lore("§7Clique para entrar.").build());
		
		menu.open(player);
	}
	
	public static void minigames(Player player) {
		MenuInventory menu = new MenuInventory("§eWarps - MiniGames", 3, true);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		menu.setItem(11, itemBuilder.type(Material.FIREWORK).name("§cKangaroo").lore("§7Clique para entrar.").build());
		menu.setItem(12, itemBuilder.type(Material.LEASH).name("§6Grappler").lore("§7Clique para entrar.").build());
		menu.setItem(13, itemBuilder.type(Material.FISHING_ROD).name("§eFisherman").lore("§7Clique para entrar.").build());
		
		menu.open(player);
	}
}
