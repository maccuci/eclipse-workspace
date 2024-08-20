package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class KangarooKit extends Kit {
	
	public KangarooKit() {
		super("Kangaroo", Material.FIREWORK, new ItemStack(Material.FIREWORK), "§7Clique em seu foguete para ganhar um boost para cima se apertado shift ganhe o mesmo boost só que para cima.", 6);
	}

}
