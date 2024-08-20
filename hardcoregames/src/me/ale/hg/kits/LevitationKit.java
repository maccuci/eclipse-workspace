package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class LevitationKit extends Kit {
	
	public LevitationKit() {
		super("Levitation", Material.TORCH, new ItemStack(Material.TORCH), "§7Controle a gravidade dos jogadores ao seu redor, fazendo eles voarem.", 5);
	}

}
