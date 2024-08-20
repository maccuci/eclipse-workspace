package me.tony.he4rt.cosmetics.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.tony.he4rt.cosmetics.api.TonyMCAPI;
import me.tony.he4rt.cosmetics.api.menu.ClickType;
import me.tony.he4rt.cosmetics.api.menu.MenuClickHandler;
import me.tony.he4rt.cosmetics.api.menu.MenuInventory;
import me.tony.he4rt.cosmetics.api.menu.MenuItem;
import me.tony.he4rt.cosmetics.list.Category;
import me.tony.he4rt.cosmetics.list.Cosmetic;
import me.tony.he4rt.cosmetics.list.CosmeticsManager;

public class CosmeticsMenu {
	
	static int itemsPerPage = 21;
	private static List<Cosmetic> cosmetics = new ArrayList<>(CosmeticsManager.getCosmetics().values());
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§8Cosméticos", 6);
		TonyMCAPI itemBuilder = new TonyMCAPI();
		
		menu.setItem(11, itemBuilder.type(Material.BONE).name("§aPets").lore("§7Crie e cuide do seu pet!").build());
		menu.setItem(new MenuItem(itemBuilder.type(Material.HOPPER).name("§aGadgets").lore("§7Experimente as nossas engenhocas.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				gadgets(p, 1);
				p.playSound(p.getLocation(), Sound.CLICK, 2.5F, 2.5F);
			}
		}), 13);
		menu.setItem(15, itemBuilder.type(Material.LEATHER_CHESTPLATE).name("§aVestimentas").lore("§7Personalize o seu personagem com", "as nossas vestimentas!").build());
		menu.setItem(31, itemBuilder.type(Material.NETHER_STAR).name("§aPartículas").lore("§7Deixe o mapa mais chamativo com", "§7as suas partículas.").build());
		menu.setItem(49, itemBuilder.type(Material.SKULL_ITEM).durability(3).skin(player.getName()).name("§aCosméticos").lore("§7Você possui: §eTODOS §7os cosméticos.").build());
		
		menu.open(player);
	}
	
	private static void gadgets(Player player, int page) {
		MenuInventory menu = new MenuInventory("§8Cosméticos - Gadgets", 6);
		TonyMCAPI itemBuilder = new TonyMCAPI();
		List<MenuItem> items = new ArrayList<>();
		Collections.sort(cosmetics, new Comparator<Cosmetic>() {

			@Override
			public int compare(Cosmetic o1, Cosmetic o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(int i = 0; i < cosmetics.size(); i++) {
			Cosmetic cosmetic = cosmetics.get(i);
			
			if(cosmetic.getCategory() == Category.GADGETS) {
				items.add(new MenuItem(itemBuilder.type(cosmetic.getIcon()).name(cosmetic.getName()).lore(cosmetic.getDescription()).build(), new MenuClickHandler() {
					
					@Override
					public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
						p.getInventory().setItem(4, itemBuilder.type(cosmetic.getIcon()).name("§a" + cosmetic.getName() + "§7(Clique)").build());
					}
				}));
			}
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
			menu.setItem(new MenuItem(itemBuilder.type(Material.ARROW).name("§aAnterior").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
					gadgets(p, page - 1);
				}
			}), 48);
		}
		
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(itemBuilder.type(Material.ARROW).name("§aPróxima").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
					gadgets(p, page + 1);
				}
			}), 50);
		}
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.SKULL_ITEM).durability(3).skin(player.getName()).name("§aCosméticos").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				open(p);
			}
		}), 49);
		
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
}
