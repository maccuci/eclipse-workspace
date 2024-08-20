package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class BackupKit extends Kit {
	
	public BackupKit() {
		super("Backup", Material.CHEST, new ItemStack(Material.CHEST), "§7Tenha um baú que você pode guardar seus itens nele.", 6);
	}

}
