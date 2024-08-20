package com.fuzion.lobby.manager.menu;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.bungee.BungeeAPI;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.ClickType;
import com.fuzion.core.api.menu.MenuClickHandler;
import com.fuzion.core.api.menu.MenuInventory;
import com.fuzion.core.api.menu.MenuItem;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.lobby.Main;

public class ServersMenu {
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§0Servidores", 3, true);
		
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.DIAMOND_SWORD).name("§eServidor de KitPvP").lore("§7Que tal você antes de entrar no hardcoregames, treinar as suas habilidades aqui?\n\n §7Jogadores: §b0").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				BungeeAPI.of(Main.getPlugin()).connect(player, "pvp");
				player.sendMessage("§aConectando ao servidor §fPVP§a.");
				player.closeInventory();
			}
		}), 11);
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.MUSHROOM_SOUP).name("§eServidor de HardcoreGames").lore("§7Modo de jogo do estilo battle royale. Crie seus itens, escolha habilidades que lhe ajudaram na partida, e o mais importante, tente ser o último de pé, para garantir a sua vitória.\n\n §7Jogadores: §b0").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				BungeeAPI.of(Main.getPlugin()).connect(player, "hg" + new Random().nextInt(3));
				player.sendMessage("§aConectando ao servidor §fHG§a.");
				player.closeInventory();
				
			}
		}), 13);
		menu.setItem(new MenuItem(new ItemBuilder().type(Material.CAKE).name("§eServidor de §dParty").lore("§7Venha para festa! Só estamos esperando você, para a mesma começar!\n\n §cAinda está por vir, aguarde...").glow().build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				player.closeInventory();
				if(!new GroupManager().hasGroup(player.getUniqueId(), Group.DEVELOPER)) {
					player.sendMessage("§cVocê não é o desenvolvedor, para entrar neste servidor.");
					return;
				}
				//player.sendMessage("§cEspera ai, este servidor ainda está sendo feito! Em breve ele estará aberto para vocês.");
			}
		}), 15);
		
		menu.open(player);
	}

}
