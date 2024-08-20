package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class KangarooKit extends Kit {

	public KangarooKit() {
		super("Kangaroo", Material.FIREWORK, new ItemStack[] {new ItemStack(Material.FIREWORK)}, "§7Use seu foguete para ter duplo pulo se você", "§7apertou shift.");
	}

}
