package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class AirBeamKit extends Kit {
	
	public AirBeamKit() {
		super("Airbender", Material.WOOL, new ItemStack[] {new ItemStack(Material.WOOL)}, "§7Crie um impulso de ar tão forte que", "§7empurre os seus oponentes.");
	}

}
