package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class InvokerKit extends Kit {
	
	public InvokerKit() {
		super("Invoker", Material.MONSTER_EGG, new ItemStack[] {new ItemStack(Material.MONSTER_EGG)}, "§7Invoque criaturas de outro mundo.");
	}

}
