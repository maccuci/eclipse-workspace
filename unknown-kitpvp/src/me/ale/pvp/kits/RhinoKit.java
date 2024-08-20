package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class RhinoKit extends Kit {
	
	public RhinoKit() {
		super("Rhino", Material.MINECART, new ItemStack[] {new ItemStack(Material.MINECART)}, "§7Tenha a velocidade de um cavalo e a", "§7força de um iron golem.");
	}

}
