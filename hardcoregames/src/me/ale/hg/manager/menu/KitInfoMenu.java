package me.ale.hg.manager.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.api.item.ItemBuilder;
import me.ale.hg.api.menu.ClickType;
import me.ale.hg.api.menu.MenuClickHandler;
import me.ale.hg.api.menu.MenuInventory;
import me.ale.hg.api.menu.MenuItem;
import me.ale.hg.event.player.PlayerSelectKitEvent;
import me.ale.hg.manager.kit.Kit;

public class KitInfoMenu {
	
	private static final ItemBuilder builder = new ItemBuilder();
	
	public static void open(Player player, Kit kit) {
		MenuInventory menu = new MenuInventory("§0Informações do kit " + kit.getName(), 6);
		Material icon = kit.getIcon();
		ItemStack item = kit.getItems();
		String lore = kit.getLore();
		boolean permission = player.hasPermission("kit." + kit.getName());
		
		for(int i = 0; i < 54; i++) {
			menu.setItem(i, builder.type(Material.THIN_GLASS).name(" ").build());
		}
		
		menu.setItem(8, new MenuItem(builder.type(Material.EMERALD_BLOCK).name("§aSelecionar kit.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				PlayerSelectKitEvent event = new PlayerSelectKitEvent(p, kit);
				Bukkit.getPluginManager().callEvent(event);
			}
		}));
		
		menu.setItem(13, builder.type(icon).name("§aKit " + kit.getName()).build());
		menu.setItem(28, builder.type(Material.BOOK).name("§aDescrição").lore(lore).build());
		menu.setItem(31, builder.type(Material.INK_SACK).name(permission ? "§aVocê possui esse kit." : "§cVocê não possui esse kit.").durability(permission ? 10 : 8).build());
		menu.setItem(34, builder.type(Material.SIGN).name("§aItens do kit").build());
		menu.setItem(43, builder.type(item == null ? Material.THIN_GLASS : item.getType()).name("§aItem do kit " + kit.getName()).build());
		
		menu.open(player);
	}

}
