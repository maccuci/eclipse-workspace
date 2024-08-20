package com.fuzion.lobby.manager.armor;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.lobby.Main;

public class ArmorManager {
	
	public static int k = 0;
	
	public static void setArmor(Player player) {
		k = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			private Random r = new Random();
			@Override
			public void run() {
				Color c = Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255));
				
/*				if (player.getInventory().getHelmet() != null
						&& player.getInventory().getHelmet().getType() == Material.LEATHER_HELMET) {
					player.getInventory().setHelmet(getColorArmor(Material.LEATHER_HELMET, c));
				}

				if (player.getInventory().getChestplate() != null
						&& player.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE) {
					player.getInventory().setChestplate(getColorArmor(Material.LEATHER_CHESTPLATE, c));
				}

				if (player.getInventory().getLeggings() != null
						&& player.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) {
					player.getInventory().setLeggings(getColorArmor(Material.LEATHER_LEGGINGS, c));
				}

				if (player.getInventory().getBoots() != null
						&& player.getInventory().getBoots().getType() == Material.LEATHER_BOOTS) {
					player.getInventory().setBoots(getColorArmor(Material.LEATHER_BOOTS, c));
				}*/
				player.getInventory().setHelmet(new ItemBuilder().type(Material.STAINED_GLASS).durability(c.asBGR()).build());
				player.getInventory().setChestplate(getColorArmor(Material.LEATHER_CHESTPLATE, c));
				player.getInventory().setLeggings(getColorArmor(Material.LEATHER_LEGGINGS, c));
				player.getInventory().setBoots(getColorArmor(Material.LEATHER_BOOTS, c));
				if (player.getInventory().getBoots() != null) {
					
				}
			}
		}, 0, 8);
	}
	
	public static void removeArmor(Player player) {
		Bukkit.getScheduler().cancelTask(k);
		player.getInventory().setArmorContents(null);
	}

	private static ItemStack getColorArmor(Material m, Color c) {
		ItemStack i = new ItemStack(m, 1);
		LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
		meta.setColor(c);
		i.setItemMeta(meta);
		return i;
	}
}
