package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class GrenadierKit extends Kit {
	
	public GrenadierKit() {
		super("Grenadier", Material.FIREWORK_CHARGE, new ItemStack(Material.FIREWORK_CHARGE), "§7Jogue uma granada altamente destrutiva no mapa.", 6);
	}

}
