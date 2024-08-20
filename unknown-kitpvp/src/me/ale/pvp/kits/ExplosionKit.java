package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class ExplosionKit extends Kit {
	
	public ExplosionKit() {
		super("Explosion", Material.ARROW, new ItemStack[] {new ItemStack(Material.ARROW)}, "§7Clique na flecha para fazer uma explosão", "§7de flechas.");
	}

}
