package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class DrainKit extends Kit {
	
	public DrainKit() {
		super("Drain", Material.SHEARS, new ItemStack(Material.SHEARS), "§7Drene a vida de todos os jogadores a 4 blcos de distancia.", 5);
	}

}
