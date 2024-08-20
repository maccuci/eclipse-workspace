package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class WizardKit extends Kit {
	
	public WizardKit() {
		super("Wizard", Material.DIAMOND_HOE, new ItemStack[] {new ItemStack(Material.DIAMOND_HOE)}, "§7Lance seus feitiços nos seus oponentes.");
	}

}
