package me.ale.hg.manager.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.Main;
import me.ale.hg.api.item.ItemBuilder;
import me.ale.hg.api.menu.ClickType;
import me.ale.hg.api.menu.MenuClickHandler;
import me.ale.hg.api.menu.MenuInventory;
import me.ale.hg.api.menu.MenuItem;
import me.ale.hg.manager.customkit.CustomKitManager;
import me.ale.hg.manager.kit.Kit;
import me.ale.hg.manager.kit.KitManager;

public class CustomKitMenu {
	
	private static int itemsPerPage = 21;
	
	private static final ItemBuilder builder = new ItemBuilder();
	
	public void open(Player player) {
		MenuInventory menu = new MenuInventory("§0Seus Custom Kits", 6);
		CustomKitManager customKit = Main.getPlugin().getCustomKit();
		
		menu.setItem(19, new MenuItem(builder.type(Material.STAINED_GLASS_PANE).durability(5).name("§aEditar " + customKit.getKitName(player)).build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				edit(p);
			}
		}));
		
		menu.open(player);
	}
	
	public void edit(Player player) {
		MenuInventory menu = new MenuInventory("§0Edite seu kit", 3);
		CustomKitManager customKit = Main.getPlugin().getCustomKit();
		
		menu.setItem(10, builder.type(customKit.getItemType(player)).name("§6§lITEM DO KIT").lore("§7Clique para alterar.").build());
		menu.setItem(11, builder.type(Material.NAME_TAG).name("§3§lNOME DO KIT").lore("§7Clique para alterar.").build());
		menu.setItem(13, builder.type(Material.NETHER_STAR).name("§b§lPONTOS").lore("§7Seus pontos: §b" + customKit.getPoints(player) + ".0").build());
		menu.setItem(14, new MenuItem(builder.type(Material.ENDER_CHEST).name("§a§lADICIONAR HABILIDADE").lore("§7Clique para adicionar.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				listKits(p, 0);
			}
		}));
		menu.setItem(15, new MenuItem(builder.type(Material.BOOK).name("§e§lLISTA DAS HABILIDADES").lore("§7Clique para ver.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				listyourkits(p, menu);
			}
		}));
		
		menu.open(player);
	}
	
	public void listyourkits(Player player, MenuInventory topInventory) {
		MenuInventory menu = new MenuInventory("§0Lista das suas habilidades", 3);
		CustomKitManager customKit = Main.getPlugin().getCustomKit();
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.REDSTONE).name("§4Voltar").build(), new MenuClickHandler() {
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				if (topInventory != null)
					topInventory.open(arg0);
				else
					arg0.closeInventory();
			}
		}), 0);
		
		for(int i = 9; i < 17; i++) {
				Kit kit = KitManager.getKit(customKit.getKitAbilities(player).replace(",", ""));
				menu.setItem(i, builder.type(kit.getIcon()).name(" ").lore("§7Nome: §c" + kit.getName()).build());
		}
		
		menu.open(player);
	}
	
	public void listKits(Player player, int page) {
		CustomKitManager customKit = Main.getPlugin().getCustomKit();
		MenuInventory menu = new MenuInventory("§0Escolha algum kit [§" + (customKit.getPoints(player) >= 25 ? "c" + customKit.getPoints(player) : "a" + customKit.getPoints(player)) + "/25]", 6);
		List<MenuItem> items = new ArrayList<>();
		
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		Collections.sort(kits, new Comparator<Kit>() {

			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		for (int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			//Ability ability = CustomKitManager.getKit(kit.getName());
			
				items.add(new MenuItem(builder.type(kit.getIcon()).name("§a" + kit.getName()).lore(kit.getLore()).build(), new MenuClickHandler() {
					
					@Override
					public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
						
						if(customKit.getPoints(p) >= 25) {
							p.sendMessage("§cVocê não pode ultrapassar a marca de 25 pontos no editor de kits.");
							p.closeInventory();
							return;
						}
						customKit.addAbility(p, kit.getName());
						customKit.setPoints(p, kit.getPoints());
						p.sendMessage("§aVocê adicionou a habilidade " + kit.getName() + " no seu customkit.");
						listKits(p, page);
					}
				}));
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
			menu.setItem(0, new MenuItem(builder.type(Material.CARPET).durability(5).name("§aPágina anterior").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
					listKits(p, page - 1);
				}
			}));
		}
		
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(8, new MenuItem(builder.type(Material.CARPET).durability(5).name("§aPágina posterior").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
					listKits(p, page + 1);
				}
			}));
		} else {
			menu.setItem(8, builder.type(Material.CARPET).durability(14).name("§cPágina posterior").build());
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
		
		if (items.size() == 0) {
			menu.setItem(new ItemBuilder().type(Material.ITEM_FRAME).name("§cVocê não tem kit.").build(), 31);
		}
		
		menu.open(player);
	}
}
