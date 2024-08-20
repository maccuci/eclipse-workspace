package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.pvp.manager.kit.Kit;

public class TeleporterKit extends Kit {
	
	public TeleporterKit() {
		super("Teleporter", Material.EYE_OF_ENDER, new ItemStack[] {new ItemStack(Material.EYE_OF_ENDER)}, "§7Clique no seu item para teleportar", "§7para onde você olha.");
	}

}
