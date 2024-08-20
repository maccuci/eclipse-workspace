package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class ZeusKit extends Kit {
	
	public ZeusKit() {
		super("Zeus", Material.BOW, new ItemStack[] {new ItemStack(Material.BOW)}, "§7Tenha um arco com o poder de onde a flecha", "§7lançada cair invoque um raio.");
	}

}
