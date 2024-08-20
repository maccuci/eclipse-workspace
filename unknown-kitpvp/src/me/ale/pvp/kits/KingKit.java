package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class KingKit extends Kit {
	
	public KingKit() {
		super("King", Material.GOLD_INGOT, new ItemStack[] {new ItemStack(Material.GOLD_INGOT)}, "§7Ao usar o seu kit deixe todos que estão em volta", "§7(raio de 4 blocos) atordoados com a sua vontade.");
	}

}
