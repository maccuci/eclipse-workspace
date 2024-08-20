package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class MinionsKit extends Kit {
	
	public MinionsKit() {
		super("Minions", Material.GOLD_SPADE, new ItemStack[] {new ItemStack(Material.GOLD_SPADE)}, "§7Clique no item para invocar seus minions", "§7e mande eles atacarem os seus oponentes.");
	}

}
