package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class VacuumKit extends Kit {
	
	public VacuumKit() {
		super("Vacuum", Material.EYE_OF_ENDER, new ItemStack(Material.EYE_OF_ENDER), "§7Puxe todos os jogadores a sua volta.", 3);
	}

}
