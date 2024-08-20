package com.fuzion.lobby.manager.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.ClickType;
import com.fuzion.core.api.menu.MenuClickHandler;
import com.fuzion.core.api.menu.MenuInventory;
import com.fuzion.core.api.menu.MenuItem;
import com.fuzion.lobby.manager.armor.ArmorManager;
import com.fuzion.lobby.manager.pets.PetsManager;
import com.fuzion.lobby.manager.wings.WingData;
import com.fuzion.lobby.manager.wings.WingManager;
import com.fuzion.lobby.manager.wings.WingManager.WingType;
import com.fuzion.lobby.manager.wings.types.AngelWing;
import com.fuzion.lobby.manager.wings.types.BloodWing;
import com.fuzion.lobby.manager.wings.types.EagleWing;
import com.fuzion.lobby.manager.wings.types.EternalWing;

public class CosmeticMenu {
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§0Cosméticos", 3, true);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.WATCH).name("§d§lAsas").lore("§7Selecione alguma asa para enfeitar o seu jogo.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				wings(arg0);
			}
		}), 11);
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.BONE).name("§a§lPets").lore("§7Selecione algum mob para ser seu pet.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				pets(player);
			}
		}), 13);
		menu.setItem(new MenuItem( new ItemBuilder().type(Material.LEATHER_CHESTPLATE).name("§6§lRoupas").lore("§7Selecione alguma roupa para ficar mais elegante.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				roupas(arg0);
			}
		}),15);
		
		menu.open(player);
	}
	
	public static void pets(Player player) {
		MenuInventory menu = new MenuInventory("§0Pets", 3, true);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.MONSTER_EGG).name("§aLobo").durability(95).build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				if(!new PetsManager().mapPet.containsKey(player)) {
					new PetsManager().addPet(arg0, arg0.getName(), EntityType.WOLF);
					player.sendMessage("§aVocê spawnou o seu pet Lobo!");
					return;
				}
				new PetsManager().removePet(arg0, EntityType.WOLF);
				player.sendMessage("§cVocê removeu o seu pet atual!");
			}
		}), 13);
		
		menu.open(player);
	}
	
	public static void roupas(Player player) {
		MenuInventory menu = new MenuInventory("§0Roupas", 3, true);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.LEATHER_CHESTPLATE).name("§aColorido").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				ArmorManager.setArmor(player);
			}
		}), 13);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.REDSTONE_BLOCK).name("§cRemover roupa").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				ArmorManager.removeArmor(player);
			}
		}), 26);
		
		menu.open(player);
	}
	
	
	public static void wings(Player player) {
		MenuInventory menu = new MenuInventory("§0Asas", 3, true);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.PAPER).name("§7Asas da §8Morte").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				if(WingManager.wingMap.containsKey(player)) {
					player.sendMessage("§cVocê já tem uma asa ativa!");
					return;
				}
				new EagleWing().display(player, new WingData(arg0, 1));
				WingManager.wingMap.put(player, WingType.DEATH.name());
			}
		}), 11);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.PAPER).name("§7Asas de §4Sangue").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				if(WingManager.wingMap.containsKey(player)) {
					player.sendMessage("§cVocê já tem uma asa ativa!");
					return;
				}
				new BloodWing().display(player, new WingData(arg0, 2));
				WingManager.wingMap.put(player, WingType.BLOOD.name());
			}
		}), 12);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.PAPER).name("§7Asas §5Eternal").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				if(WingManager.wingMap.containsKey(player)) {
					player.sendMessage("§cVocê já tem uma asa ativa!");
					return;
				}
				new EternalWing().display(player, new WingData(arg0, 3));
				WingManager.wingMap.put(player, WingType.ETERNAL.name());
			}
		}), 13);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.PAPER).name("§7Asas de §fAnjo").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				if(WingManager.wingMap.containsKey(player)) {
					player.sendMessage("§cVocê já tem uma asa ativa!");
					return;
				}
				new AngelWing().display(player, new WingData(arg0, 3));
				WingManager.wingMap.put(player, WingType.ANGEL.name());
			}
		}), 14);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.REDSTONE_BLOCK).name("§cRemover Asas").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				if(WingManager.wingMap.get(player) == WingType.DEATH.name()) {
					player.closeInventory();
					WingManager.wingMap.remove(player);
					player.sendMessage("§cVocê removeu as suas asas!");
					Bukkit.getScheduler().cancelTask(EagleWing.k);
				} else if(WingManager.wingMap.get(player) == WingType.BLOOD.name()) {
					player.closeInventory();
					WingManager.wingMap.remove(player);
					player.sendMessage("§cVocê removeu as suas asas!");
					Bukkit.getScheduler().cancelTask(BloodWing.k);
				} else if(WingManager.wingMap.get(player) == WingType.ETERNAL.name()) {
					player.closeInventory();
					WingManager.wingMap.remove(player);
					player.sendMessage("§cVocê removeu as suas asas!");
					Bukkit.getScheduler().cancelTask(EternalWing.k);
				} else if(WingManager.wingMap.get(player) == WingType.ANGEL.name()) {
					player.closeInventory();
					WingManager.wingMap.remove(player);
					player.sendMessage("§cVocê removeu as suas asas!");
					Bukkit.getScheduler().cancelTask(AngelWing.k);
				}
			}
		}), 26);
		
		menu.open(player);
	}
}
