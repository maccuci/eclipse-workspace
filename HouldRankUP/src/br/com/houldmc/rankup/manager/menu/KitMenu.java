package br.com.houldmc.rankup.manager.menu;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.houldmc.rankup.api.cooldown.CooldownAPI;
import br.com.houldmc.rankup.api.cooldown.types.Cooldown;
import br.com.houldmc.rankup.api.date.DateUtils;
import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.api.menu.ClickType;
import br.com.houldmc.rankup.api.menu.MenuClickHandler;
import br.com.houldmc.rankup.api.menu.MenuInventory;
import br.com.houldmc.rankup.api.menu.MenuItem;
import br.com.houldmc.rankup.manager.kit.Kit;
import br.com.houldmc.rankup.manager.kit.KitManager;

public class KitMenu {
	
	public static void principal(Player player) {
		MenuInventory menu = new MenuInventory("§8Kits", 6);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		for(int i = 0; i < 9; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.LOG).name("§eMembro").lore("§7Clique para ir até a seção de kits para membros.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				giveKit(p, "Membro", menu);
			}
		}), 20);
		menu.setItem(24, itemBuilder.type(Material.DIAMOND_BLOCK).name("§eVip").lore("§7Clique para ir até a seção de kits para vips.").build());
		menu.setItem(22, itemBuilder.type(Material.STONE_PICKAXE).name("§eRanks").lore("§7Clique para ir até a seção de kits de ranks.").build());
		menu.open(player);
	}
	
	public static void member(Player player, MenuInventory lastInventory) {
		MenuInventory menu = new MenuInventory("§8Kits - Membro", 6);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.CARPET).durability(14).name("§cVoltar").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				if(lastInventory != null) {
					lastInventory.open(p);
				}
			}
		}), 0);
		
		for(int i = 9; i < 18; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		
		menu.setItem(3, itemBuilder.type(Material.WOOL).name("§cNão pegar").durability(14).build());
		menu.setItem(4, itemBuilder.type(Material.PAPER).name("§bInformações").lore("§fCooldown: §71 Dia.", "§fItens: §7Espada de Ferro", " §8(Afiada X & §8Inquebrável III)", "§7Armadura de Chain", "§8(Proteção XX)", "§7x25 Maçãs douradas").build());
		if(CooldownAPI.hasCooldown(player, "member-kit")) {
			menu.setItem(5, itemBuilder.type(Material.BARRIER).name("§cPegar Kit").lore("§7Você não pode pegar este kit durante", "§f" + DateUtils.formatDifference((int)CooldownAPI.getCooldown(player).getRemaining())).build());
		} else {
			menu.setItem(new MenuItem(itemBuilder.type(Material.WOOL).name("§aPegar Kit").durability(5).build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
					p.closeInventory();
					p.sendMessage("§aVocê pegou o kit Membro diário.");
					p.getInventory().addItem(itemBuilder.type(Material.IRON_SWORD).enchantment(Enchantment.DAMAGE_ALL, 10).enchantment(Enchantment.DURABILITY, 3).build());
					p.getInventory().addItem(itemBuilder.type(Material.CHAINMAIL_HELMET).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20).enchantment(Enchantment.DURABILITY, 3).build());
					p.getInventory().addItem(itemBuilder.type(Material.CHAINMAIL_CHESTPLATE).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20).enchantment(Enchantment.DURABILITY, 3).build());
					p.getInventory().addItem(itemBuilder.type(Material.CHAINMAIL_LEGGINGS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20).enchantment(Enchantment.DURABILITY, 3).build());
					p.getInventory().addItem(itemBuilder.type(Material.CHAINMAIL_BOOTS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20).enchantment(Enchantment.DURABILITY, 3).build());
					p.getInventory().addItem(itemBuilder.type(Material.GOLDEN_APPLE).amount(25).build());
					CooldownAPI.addCooldown(p, new Cooldown("member-kit", 86400L));
				}
			}), 5);
		}
		
		for(ItemStack test : KitManager.getKit("Member").getItems()) {
			menu.addItem(test);
		}
		
		menu.open(player);
	}
	
	public static void giveKit(Player player, String kitName, MenuInventory lastInventory) {
		Kit kit = KitManager.getKitTest(kitName);
		MenuInventory menu = new MenuInventory("§8Kits - " + kit.getName(), 6);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.CARPET).durability(14).name("§cVoltar").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				if(lastInventory != null) {
					lastInventory.open(p);
				}
			}
		}), 0);
		
		for(int i = 9; i < 18; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
			for(ItemStack test : kit.getItems()) {
				menu.addItem(test);
		}
		}
		
		menu.open(player);
	}
}
