package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class SirializerKit extends Kit {
	
	public SirializerKit() {
		super("Sirializer", Material.BONE, new ItemStack[] {new ItemStack(Material.BONE)}, "§7Todos os seus oponentes dentro de um raio de 4 blocos", "§7tomaram um dano muito forte.");
	}

}
