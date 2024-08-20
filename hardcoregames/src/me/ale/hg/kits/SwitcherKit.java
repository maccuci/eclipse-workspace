package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class SwitcherKit extends Kit {
	
	public SwitcherKit() {
		super("Switcher", Material.SNOW_BALL, new ItemStack(Material.SNOW_BALL), "§7Troque de lugar com quem a sua bola de neve acertou.", 3);
	}

}
