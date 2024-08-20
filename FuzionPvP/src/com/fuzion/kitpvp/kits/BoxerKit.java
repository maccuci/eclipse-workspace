package com.fuzion.kitpvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.kitpvp.manager.kit.Kit;

public class BoxerKit extends Kit {
	
	public BoxerKit() {
		super("Boxer", new ItemStack(Material.STONE_SWORD), Group.BETA, "§7Receba menos dano, e dê mais dano.");
	}

}
