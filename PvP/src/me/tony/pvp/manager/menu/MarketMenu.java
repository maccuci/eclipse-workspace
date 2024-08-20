package me.tony.pvp.manager.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.tony.commons.api.item.ItemBuilder;
import me.tony.commons.api.menu.ClickType;
import me.tony.commons.api.menu.MenuClickHandler;
import me.tony.commons.api.menu.MenuInventory;
import me.tony.commons.api.menu.MenuItem;
import me.tony.pvp.manager.kit.Kit;
import me.tony.pvp.manager.kit.KitManager;

public class MarketMenu {
	
	public static void normal(Player player) {
		MenuInventory menu = new MenuInventory("§aMercado", 3, true);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		for(int i = 0; i < 27; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(5).name(" ").build());
			menu.setItem(new MenuItem(itemBuilder.type(Material.DIAMOND_SWORD).name("§aHabilidades").hideAtributes().lore("§7Clique para entrar na seção de habilidades!", "§7Cada habilidade que adquirir é temporária.").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					normal_abilities(arg0, 1);
				}
			}), 12);
			menu.setItem(14, itemBuilder.type(Material.POTION).glow().name("§aBoosters").hideAtributes().lore("§7Clique para entrar na seção de boosters!", "§7Cada booster que adquirir é temporário.").build());
		}
		
		menu.open(player);
	}
	
	public static void normal_abilities(Player player, int page) {
		MenuInventory menu = new MenuInventory("§aMercado > Habilidades", 6, true);
		ItemBuilder itemBuilder = new ItemBuilder();
		int itemsPerPage = 21;
		
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		List<MenuItem> items = new ArrayList<>();
		Collections.sort(kits, new Comparator<Kit>() {

			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			menu.setItem(4, itemBuilder.type(Material.PAPER).name("§bAjuda").lore("§7Bem-vindo ao meu mercado, nele você pode", "§7encontrar diversos tipos de produtos", "§7mas todos eles são temporarios.", " ", "§7Seção de: §bHabilidades.").build());
			
			items.add(new MenuItem(itemBuilder.type(kit.getIcon()).name("§f§l» §7Kit §f" + kit.getName()).lore(kit.getLore()).lore(" ", "§aPreço: §f" + kit.getPrice()).build()));
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
					normal_abilities(arg0, page - 1);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.STAINED_GLASS_PANE).name("§aPágina Posterior").lore("§7" + page + " / " + pageEnd).durability(5).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					normal_abilities(arg0, page + 1);
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
	
	public static void nocture(Player player) {
		MenuInventory menu = new MenuInventory("§5Mercado Nortuno", 3, true);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		for(int i = 0; i < 27; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
			menu.setItem(13, itemBuilder.type(Material.RECORD_11).name("§7§k||0||0||0||0").hideAtributes().glow().hideAtributes().lore("§4Saia! O mercado noturno está fechado.", " ","§7§oVula kuzo zonke izenzakalo.").build());
		}
		
		menu.open(player);
	}
	
	public static void nocture_open(Player player) {
		MenuInventory menu = new MenuInventory("§5Mercado Nortuno", 3, true);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		for(int i = 0; i < 27; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
			menu.setItem(20, itemBuilder.type(Material.RECORD_6).name("§f§l» §7Kit §fShuizen").lore("§7Compre-o por 4500 Coins! §e§lPREÇO ÚNICO!").build());
			menu.setItem(0, itemBuilder.type(Material.GOLD_INGOT).name("§a1 Nível").glow().lore("§7Compre-o por 1500 Coins!", " ", "§7Acompanha: §a1 Nível", "§c§lITEM ESPECIAL!").build());
			menu.setItem(16, itemBuilder.type(Material.CHEST).name("§aBáu de XPs").glow().lore("§7Compre-o por 3000 Coins! ", " ", "§7Acompanha: §a500xp", "§c§lITEM ESPECIAL!").build());
			menu.setItem(13, itemBuilder.type(Material.RECORD_11).name("§7§k||0||0||0||0").hideAtributes().glow().hideAtributes().lore("§aVenha! O mercado noturno está aberto.", " ","§7§oVula kuzo zonke izenzakalo.").build());
		}
		
		menu.open(player);
	}
}
