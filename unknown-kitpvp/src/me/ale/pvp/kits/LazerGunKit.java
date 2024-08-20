package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class LazerGunKit extends Kit {
	
	@SuppressWarnings("deprecation")
	public LazerGunKit() {
		super("LazerGun", Material.getMaterial(418), new ItemStack[] {new ItemStack(Material.getMaterial(418))}, "§7Tenha uma arma que ativar lazer.");
	}

}
