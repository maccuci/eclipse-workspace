package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class ThermoKit extends Kit {
	
	public ThermoKit() {
		super("Thermo", "Contra times", new ItemStack(Material.DAYLIGHT_DETECTOR), Group.BETA, "§7Transforme água em lava e lava em água.");
		addItem(new ItemStack(Material.DAYLIGHT_DETECTOR, 1));
	}

}
