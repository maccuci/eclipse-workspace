package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class PickpocketKit extends Kit {
	
	public PickpocketKit() {
		super("Pickpocket", Material.BLAZE_ROD, new ItemStack(Material.BLAZE_ROD), "§7Roube 3 itens do inventário de algum jogador.", 15);
	}

}
