package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.tony.pvp.manager.kit.Kit;

public class ShuizenKit extends Kit {
	
	public ShuizenKit() {
		super("Shuizen", Material.DIAMOND, new ArrayList<ItemStack>(), 3000, "§7Clique em seu diamante para criar um tornado.");
	}

}
