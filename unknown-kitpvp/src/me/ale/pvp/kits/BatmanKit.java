package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class BatmanKit extends Kit {
	
	public BatmanKit() {
		super("Batman", Material.COAL, new ItemStack[] {new ItemStack(Material.COAL)}, "§7Clique no carvão para invocar morcegos", "§7explosivos e venenosos!");
	}

}
