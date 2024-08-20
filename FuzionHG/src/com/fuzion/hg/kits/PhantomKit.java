package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class PhantomKit extends Kit {
	
	public PhantomKit() {
		super("Phantom", "Maratonista", new ItemStack(Material.FEATHER), Group.BETA, "§7Clique na sua pena para voar durante alguns segundos.");
		addItem(new ItemStack(Material.FEATHER, 1));
	}

}
