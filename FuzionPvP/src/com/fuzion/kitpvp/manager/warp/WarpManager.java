package com.fuzion.kitpvp.manager.warp;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.manager.kit.KitManager;
import com.fuzion.kitpvp.manager.onevsone.Warp1v1;
import com.fuzion.kitpvp.manager.protection.ProtectionManager;

public class WarpManager {
	
	private static File file;
	private static YamlConfiguration configuration;
	
	public static HashMap<Player, Warps> warp = new HashMap<>();
	
	public static void setWarpPlayer(Player player, Warps warps) {
		warp.put(player, warps);
		System.out.println("Player is a new " + warps.getName() + " warp.");
	}
	
	public WarpManager() {
		file = new File(Main.getPlugin().getDataFolder() + "/warps.yml");
		configuration = YamlConfiguration.loadConfiguration(file);
	}
	
	public void setWarp(String name, Location location) {
		Coords coords = new Coords(location);
		
		configuration.set(name + ".X", coords.getX());
		configuration.set(name + ".Y", coords.getY());
		configuration.set(name + ".Z", coords.getZ());
		configuration.set(name + ".Pitch", coords.getPitch());
		configuration.set(name + ".Yaw", coords.getYaw());
		try {
			configuration.save(file);
		} catch (Exception e) {}
	}
	
	public static void teleport(Player player, Warps warp) {
		if (warp.equals(Warps.ONE_VS_ONE)) {
			setWarpPlayer(player, Warps.ONE_VS_ONE);
			player.teleport(getLocation("1v1Spawn"));
			ProtectionManager.addProtectionToPlayer(player);
			Warp1v1.playersIn1v1.add(player);
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
			player.getInventory().setItem(3, new ItemBuilder().type(Material.BLAZE_ROD).name("§eDesafie um Jogador").build());
			player.getInventory().setItem(4, new ItemBuilder().type(Material.WORKBENCH).name("§cDesafio Customizado").build());
			player.getInventory().setItem(5, new ItemBuilder().type(Material.INK_SACK).name("§e1v1 Rápido").durability(8).build());
			player.sendMessage("§aVocê foi teleportado até a warp 1v1.");
		} else if (warp.equals(Warps.FPS)) {
			setWarpPlayer(player, Warps.FPS);
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
			for (int i = 0; i < 36; i++) {
				player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
			}
			player.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
			player.getInventory().setItem(15, new ItemStack(Material.BOWL, 64));
			player.getInventory().setItem(16, new ItemStack(Material.BROWN_MUSHROOM, 64));
			player.getInventory().setItem(0, new ItemBuilder().name("§dStone Sword").type(Material.STONE_SWORD)
					.enchantment(Enchantment.DAMAGE_ALL).unbreakable().build());
			player.updateInventory();
		} else if (warp.equals(Warps.LAVA)) {
			setWarpPlayer(player, Warps.LAVA);
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
			for (int i = 0; i < 36; i++) {
				player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
			}
			player.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
			player.getInventory().setItem(15, new ItemStack(Material.BOWL, 64));
			player.getInventory().setItem(16, new ItemStack(Material.BROWN_MUSHROOM, 64));
		} else if(warp.equals(Warps.SPAWN)) {
			setWarpPlayer(player, Warps.SPAWN);
			Warp1v1.playersIn1v1.remove(player);
			new KitManager().removeKit(player);
			player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
			player.setFoodLevel(20);
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
			player.getInventory().setItem(3, new ItemBuilder().type(Material.CHEST).name("§aSeus Kits").build());
			player.getInventory().setItem(5, new ItemBuilder().type(Material.ENCHANTED_BOOK).name("§aWarps").build());
		}
}
	
	public static Warps getWarp(Player player) {
		return warp.get(player);
	}
	
	public static Location getLocation(String name) {
		Location location = null;
		location = new Location(Bukkit.getWorld("world"), configuration.getDouble(name + ".X"), configuration.getDouble(name + ".Y"), configuration.getDouble(name + ".Z"), configuration.getFloat(name + ".Yaw"), configuration.getFloat(name + ".Pitch"));
		
		return location;
	}
}
