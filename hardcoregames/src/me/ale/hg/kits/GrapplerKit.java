package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class GrapplerKit extends Kit {
	
	public GrapplerKit() {
		super("Grappler", Material.LEASH, new ItemStack(Material.LEASH), "§7Se transforme em um cowboy com o seu laço.", 7);
	}

}
