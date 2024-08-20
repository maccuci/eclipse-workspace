package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class StomperKit extends Kit {
	
	public StomperKit() {
		super("Stomper", Material.IRON_BOOTS, new ItemStack[] {new ItemStack(Material.IRON_BOOTS)}, "§7Converta o dano da sua queda em dano para", "§7os inimigos mais próximos.");
	}

}
