package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class MangoterKit extends Kit {
	
	public MangoterKit() {
		super("Mangoter", Material.BEACON, new ItemStack[] {new ItemStack(Material.BEACON)}, "§7Tenha o poder de invocar um totem que cura todos", "§7a sua volta em um raio de 4 blocos!");
	}

}
