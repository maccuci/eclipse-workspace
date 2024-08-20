package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class SupersaiyanKit extends Kit {

	public SupersaiyanKit() {
		super("Super Saiyan", Material.SNOW_BALL, new ItemStack[] {new ItemStack(Material.WOOL, 1, (short)4)}, "§7Se transforme em um super sayjin e tenha", "§7o poder de soltar um kamehameha!");
	}

}
