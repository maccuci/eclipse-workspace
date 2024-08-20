package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class MusicKit extends Kit {
	
	public MusicKit() {
		super("Music", Material.NOTE_BLOCK, new ItemStack(Material.NOTE_BLOCK), "§7Deixe todo o inimigo surdo com a sua música.", 6);
	}

}
