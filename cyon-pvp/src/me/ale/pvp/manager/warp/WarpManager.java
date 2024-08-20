package me.ale.pvp.manager.warp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.ale.commons.api.item.ItemBuilder;

public class WarpManager {
	
	static final ItemBuilder builder = new ItemBuilder();
	
	public static void senWarp(Player player, Warp warp) {
		switch (warp) {
		case FFA:
			player.teleport(warp.getLocation());
			player.getInventory().clear();
			player.getInventory().setItem(0, builder.type(Material.PAPER).name("§aClique para receber seu kit").build());
			break;
			
		case CLASSIC:
			player.teleport(warp.getLocation());
			player.getInventory().clear();
			player.getInventory().setItem(0, builder.type(Material.PAPER).name("§aClique para receber seu kit").build());
			break;
			
		case CHALLENGES_SOLO:
			player.teleport(warp.getLocation());
			player.getInventory().clear();
			player.getInventory().setItem(0, builder.type(Material.BLAZE_ROD).name("§aDesafios Solo").build());
			break;
			
		case CHALLENGES:
			player.teleport(warp.getLocation());
			player.getInventory().clear();
			player.getInventory().setItem(0, builder.type(Material.REDSTONE).name("§4Voltar").build());
			player.getInventory().setItem(3, builder.type(Material.ANVIL).name("§aSalvar localização").build());
			player.getInventory().setItem(4, builder.type(Material.ENCHANTED_BOOK).name("§eDesafios").build());
			break;
			
		case MARKET:
			player.teleport(new Location(player.getWorld(), warp.getLocation().getX(), warp.getLocation().getY() + 10, warp.getLocation().getZ()));
			player.getInventory().clear();
			player.getInventory().setItem(0, builder.type(Material.BLAZE_ROD).name("§cBastão de Troca").lore("§7Realize trocas de kits ou packs privada", "§7com o jogador que você escolheu.").build());
			break;

		default:
			break;
		}
	}

}
