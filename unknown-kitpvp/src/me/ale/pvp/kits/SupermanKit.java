package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class SupermanKit extends Kit {
	
	public SupermanKit() {
		super("Superman", Material.REDSTONE, new ItemStack[] {new ItemStack(Material.REDSTONE), new ItemStack(Material.CARPET)}, "§7Tenha dois poderes do superman!", "§7Tenha o poder da visão de calor e o poder de voar!");
	}

}
