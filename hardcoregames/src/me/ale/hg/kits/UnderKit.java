package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class UnderKit extends Kit {
	
	public UnderKit() {
		super("Under", Material.SANDSTONE, new ItemStack(Material.SANDSTONE), "§7Deixe todos ao seu redor de 4 blocos cegos e envenenados.", 5);
	}

}
