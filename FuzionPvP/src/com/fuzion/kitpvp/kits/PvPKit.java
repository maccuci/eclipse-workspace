package com.fuzion.kitpvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.kitpvp.manager.kit.Kit;

public class PvPKit extends Kit {

	public PvPKit() {
		super("PvP", new ItemStack(Material.STONE_SWORD), Group.NORMAL, "§7Venha com uma espada encantada..");
	}
	
}
