package me.ale.pvp.manager.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.ale.commons.CyonAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.api.menu.ClickType;
import me.ale.commons.api.menu.MenuClickHandler;
import me.ale.commons.api.menu.MenuInventory;
import me.ale.commons.api.menu.MenuItem;
import me.ale.pvp.event.PlayerBuyKitEvent;
import me.ale.pvp.event.PlayerSelectKit;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitManager;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.manager.kit.KitUtils;

public class KitMenu {
	
	private static int itemsPerPage = 21;
	
	static final ItemBuilder builder = new ItemBuilder();
	public static HashMap<Player, String> kitNameSelection = new HashMap<>();
	public static ArrayList<Player> kitNameCheck = new ArrayList<>();
	
	static KitType kitType;
	@Getter
	private static MenuInventory menuOld;
	
	public static void setKitType(Player player, KitType type) {
		kitType = type;
		open(player, 1, type);
	}
	
	public static void open(Player player, int page, KitType type) {
		MenuInventory menu = new MenuInventory("§8Seus Kits", 6);
		menuOld = menu;
		
		if(type == KitType.ALPHABETICAL) {
			List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
			Collections.sort(kits, new Comparator<Kit>() {
				@Override
				public int compare(Kit o1, Kit o2) {
					return o1.getName().compareTo(o2.getName());
				}

			});
			
			List<MenuItem> items = new ArrayList<>();
			menu.setItem(3, new MenuItem(builder.type(Material.BOAT).name("§bKit Type").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					if(type == KitType.ALPHABETICAL) {
						setKitType(player, KitType.RARITY);
						arg0.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o tipo para §aRaridade.");
					}
				}
			}));
			
			menu.setItem(4, new MenuItem(builder.type(Material.SIGN).name("§2Loja").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					if(type == KitType.ALPHABETICAL) {
						setKitType(player, KitType.MARKET);
						arg0.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o tipo para §aLoja.");
					}
				}
			}));
			
			menu.setItem(5, new MenuItem(builder.type(Material.COMPASS).name("§cProcurar kit").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					arg0.sendMessage(CyonAPI.SERVER_PREFIX + "Insira o nome do kit.");
					kitNameCheck.add(arg0);
					player.closeInventory();
				}
			}));
			
			for (int i = 0; i < kits.size(); i++) {
				Kit kit = kits.get(i);

				if(kit.canUseKit(player) || KitUtils.isKitFree(kit.getName())) {
					items.add(new MenuItem(builder.type(kit.getIcon()).name(kit.getRarity().getRarityColor() + kit.getName()).lore(kit.getLore()).build(), new SelectKit(kit)));
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
				menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Anterior").lore(Arrays.asList("§7Clique para ir para a página", "§7anterior")).build(), new MenuClickHandler() {
					@Override
					public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
						open(arg0, page - 1, type);
					}
				}), 27);
			}
			if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
				menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Posterior").lore(Arrays.asList("§7Clique para ir para a página", "§7posterior")).build(), new MenuClickHandler() {
					@Override
					public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
						open(arg0, page + 1, type);
					}
				}), 35);
			} else {
				menu.setItem(new ItemBuilder().type(Material.INK_SACK).durability(8).name("§cPágina Posterior").build(), 35);
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
		} else if(type == KitType.RARITY) {
			List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
			Collections.sort(kits, new Comparator<Kit>() {
				@Override
				public int compare(Kit o1, Kit o2) {
					return o1.getRarity().compareTo(o2.getRarity());
				}

			});
			
			List<MenuItem> items = new ArrayList<>();
			menu.setItem(3, new MenuItem(builder.type(Material.BOAT).name("§bKit Type").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					if(type == KitType.RARITY) {
						setKitType(player, KitType.ALPHABETICAL);
						arg0.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o tipo para §aAlfabético.");
					}
				}
			}));
			
			menu.setItem(4, new MenuItem(builder.type(Material.SIGN).name("§2Loja").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					if(type == KitType.RARITY) {
						setKitType(player, KitType.MARKET);
						arg0.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o tipo para §aLoja.");
					}
				}
			}));
			
			menu.setItem(5, new MenuItem(builder.type(Material.COMPASS).name("§cProcurar kit").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					arg0.sendMessage(CyonAPI.SERVER_PREFIX + "Insira o nome do kit.");
					kitNameCheck.add(arg0);
					player.closeInventory();
				}
			}));
			
			for (int i = 0; i < kits.size(); i++) {
				Kit kit = kits.get(i);
				
				if(kit.canUseKit(player) || KitUtils.isKitFree(kit.getName())) {
					items.add(new MenuItem(builder.type(kit.getIcon()).name(kit.getRarity().getRarityColor() + kit.getName()).lore(kit.getLore()).build(), new SelectKit(kit)));
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
				menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Anterior").lore(Arrays.asList("§7Clique para ir para a página", "§7anterior")).build(), new MenuClickHandler() {
					@Override
					public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
						open(arg0, page - 1, type);
					}
				}), 27);
			}
			if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
				menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Posterior").lore(Arrays.asList("§7Clique para ir para a página", "§7posterior")).build(), new MenuClickHandler() {
					@Override
					public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
						open(arg0, page + 1, type);
					}
				}), 35);
			} else {
				menu.setItem(new ItemBuilder().type(Material.INK_SACK).durability(8).name("§cPágina Posterior").build(), 35);
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
		} else if(type == KitType.MARKET) {
			List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
			Collections.sort(kits, new Comparator<Kit>() {
				@Override
				public int compare(Kit o1, Kit o2) {
					return o1.getRarity().compareTo(o2.getRarity());
				}

			});
			
			List<MenuItem> items = new ArrayList<>();
			menu.setItem(3, new MenuItem(builder.type(Material.BOAT).name("§bKit Type").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					if(type == KitType.MARKET) {
						setKitType(player, KitType.ALPHABETICAL);
						arg0.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o tipo para §aAlfabético.");
					}
				}
			}));
			
			menu.setItem(4, new MenuItem(builder.type(Material.SIGN).name("§2Loja").build()));
			
			menu.setItem(5, new MenuItem(builder.type(Material.COMPASS).name("§cProcurar kit").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					arg0.sendMessage(CyonAPI.SERVER_PREFIX + "Insira o nome do kit.");
					kitNameCheck.add(arg0);
					player.closeInventory();
				}
			}));
			
			for (int i = 0; i < kits.size(); i++) {
				Kit kit = kits.get(i);

				if (kit.getRarity() == KitRarity.RARE) {
					items.add(new MenuItem(
							builder.type(kit.getIcon()).name(kit.getRarity().getRarityColor() + kit.getName())
									.lore(kit.getLore()).lore(" ", "§7Compre por §61.000 Money").build(),
							new BuyKit(kit)));
				} else if (kit.getRarity() == KitRarity.MYSTIC) {
					items.add(new MenuItem(
							builder.type(kit.getIcon()).name(kit.getRarity().getRarityColor() + kit.getName())
									.lore(kit.getLore()).lore(" ", "§7Compre por §65.000 Money").build(),
							new BuyKit(kit)));
				} else if (kit.getRarity() == KitRarity.EXTRAORDINARY) {
					items.add(new MenuItem(
							builder.type(kit.getIcon()).name(kit.getRarity().getRarityColor() + kit.getName())
									.lore(kit.getLore()).lore(" ", "§7Compre por §610.000 Money").build(),
							new BuyKit(kit)));
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
				menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Anterior").lore(Arrays.asList("§7Clique para ir para a página", "§7anterior")).build(), new MenuClickHandler() {
					@Override
					public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
						open(arg0, page - 1, type);
					}
				}), 27);
			}
			if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
				menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Posterior").lore(Arrays.asList("§7Clique para ir para a página", "§7posterior")).build(), new MenuClickHandler() {
					@Override
					public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
						open(arg0, page + 1, type);
					}
				}), 35);
			} else {
				menu.setItem(new ItemBuilder().type(Material.INK_SACK).durability(8).name("§cPágina Posterior").build(), 35);
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
		}
		
		menu.open(player);
	}
	
	public static void openSearch(Player player, MenuInventory topInventory, int page, String search) {
		MenuInventory menu = new MenuInventory("§8Procurar kit: §c" + search, 6);
		List<MenuItem> items = new ArrayList<>();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.REDSTONE).name("§4Voltar").build(), new MenuClickHandler() {
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				if (topInventory != null)
					topInventory.open(arg0);
				else
					arg0.closeInventory();
			}
		}), 0);
		
		for (int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			
			if(kit.getName().contains(search)) {
				items.add(new MenuItem(builder.type(kit.getIcon()).name(kit.getRarity().getRarityColor() + kit.getName()).lore(kit.getLore()).build(), new SelectKit(kit)));
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
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Anterior").lore(Arrays.asList("§7Clique para ir para a página", "§7anterior")).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					openSearch(arg0, topInventory, page - 1, kitNameSelection.get(arg0));
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Posterior").lore(Arrays.asList("§7Clique para ir para a página", "§7posterior")).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					openSearch(arg0, topInventory, page + 1, kitNameSelection.get(arg0));
				}
			}), 35);
		} else {
			menu.setItem(new ItemBuilder().type(Material.INK_SACK).durability(8).name("§cPágina Posterior").build(), 35);
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
		
		if(items.size() == 0) {
			menu.setItem(31, builder.type(Material.ITEM_FRAME).name("§cNenhum kit encontrado.").build());
		}
		
		menu.open(player);
	}
	
	public static boolean validateName(String username) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}
	
	public static class SelectKit implements MenuClickHandler {
		private Kit kit;
		
		public SelectKit(Kit kit) {
			this.kit = kit;
		}
		
		@Override
		public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
			if(!kit.canUseKit(arg0) && !KitUtils.isKitFree(kit.getName()))
				return;
			PlayerSelectKit event = new PlayerSelectKit(arg0, kit);
			Bukkit.getPluginManager().callEvent(event);
			arg0.closeInventory();
			return;
		}
	}
	
	public static class BuyKit implements MenuClickHandler {
		private Kit kit;
		
		public BuyKit(Kit kit) {
			this.kit = kit;
		}
		
		@Override
		public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
			if(kit.getRarity() == KitRarity.RARE) {
				PlayerBuyKitEvent event = new PlayerBuyKitEvent(arg0, kit, 1000);
				Bukkit.getPluginManager().callEvent(event);
			} else if(kit.getRarity() == KitRarity.MYSTIC) {
				PlayerBuyKitEvent event = new PlayerBuyKitEvent(arg0, kit, 5000);
				Bukkit.getPluginManager().callEvent(event);
			} else if(kit.getRarity() == KitRarity.EXTRAORDINARY) {
				PlayerBuyKitEvent event = new PlayerBuyKitEvent(arg0, kit, 10000);
				Bukkit.getPluginManager().callEvent(event);
			}
			arg0.closeInventory();
			return;
		}
	}
}
