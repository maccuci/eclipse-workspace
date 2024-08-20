package me.ale.commons.bukkit.menu;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.api.menu.ClickType;
import me.ale.commons.api.menu.MenuClickHandler;
import me.ale.commons.api.menu.MenuInventory;
import me.ale.commons.api.menu.MenuItem;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.manager.FactionManager;
import me.ale.commons.core.account.manager.constructor.Faction;

public class FactionMenu {
	
	public static enum FactionSettings {
		MONEY, MESSAGE, DELETE;
	}
	
	static final ItemBuilder builder = new ItemBuilder();
	public static final HashMap<Player, FactionSettings> settings = new HashMap<>();
	
	
	public static void status(Player player) {
		MenuInventory menu = new MenuInventory("§aFaction Status", 3);
		FactionManager f = BukkitMain.getPlugin().getFactionManager();
		Faction faction = f.getFaction(player); 
		
		for(int i = 0; i < 27; i++) {
			menu.setItem(11, builder.type(Material.DIAMOND_SWORD).name("§7Kills §8» §a" + faction.getKills()).build());
			menu.setItem(13, builder.type(Material.SKULL_ITEM).name("§7Deaths §8» §c" + faction.getDeaths()).build());
			menu.setItem(1, builder.type(Material.PAPER).name("§7Money §8» §2" + faction.getMoney()).build());
			menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(7).build());
		}
		
		menu.open(player);
	}
	
	public static void settings(Player player) {
		MenuInventory menu = new MenuInventory("§aFaction Settings", 6);
		FactionManager f = BukkitMain.getPlugin().getFactionManager();
		Faction faction = f.getFaction(player);
		for(int i = 0; i < 9; i++) {
			menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(7).build());
		}
		menu.setItem(13, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(14).build());
		menu.setItem(20, new MenuItem(builder.type(Material.GOLD_BLOCK).name("§aAdicionar money").lore("§7Clique para adicionar seu money no", "§7money da faction.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				settings.put(p, FactionSettings.MONEY);
				p.closeInventory();
				p.sendMessage(CyonAPI.WARNING_PREFIX + "Insira a quantidade desejada de money.");
				return;
			}
		}));
		menu.setItem(21, builder.type(Material.PAPER).name("§aMoney da Faction").lore("§7Atualmente a faction possui §2" + faction.getMoney() + " money§7.").build());
		menu.setItem(22, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(14).build());
		menu.setItem(23, new MenuItem(builder.type(Material.SKULL_ITEM).name("§aVer membros").lore("§7Clique para ver todos os membros da faction.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				p.performCommand("faction members");
				p.closeInventory();
			}
		}));
		menu.setItem(24, builder.type(Material.REDSTONE_BLOCK).name("§aExpulsar jogador").lore("§7Escolha algum jogador da factio para você expulsar o mesmo.").build());
		menu.setItem(30, new MenuItem(builder.type(Material.BOOK).name("§aMensagem de boas vindas").lore("§7Altere a mensagem de boas vindas a faction.", " ", "§7Atual: §a" + faction.getMessage()).build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				p.sendMessage(CyonAPI.WARNING_PREFIX + "Insira a nova mensagem.");
				p.closeInventory();
				settings.put(p, FactionSettings.MESSAGE);
				return;
			}
		}));
		menu.setItem(31, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(14).build());
		menu.setItem(40, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(14).build());
		menu.setItem(45, builder.type(Material.REDSTONE).name("§cDeletar Faction").lore("§7Clique para deletar a faction. Atenção: Todo o money recolhido pela faction, serão perdidos.").build());
		menu.setItem(49, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(14).build());
		menu.setItem(53, builder.type(Material.WOOD_DOOR).name("§cSair do clan").lore("§7Clique para sair da faction. Atenção: Todo o money que você depositou §cNÃO §7voltará para você.").build());
		
		menu.open(player);
	}
}
