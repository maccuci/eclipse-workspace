package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.tony.pvp.manager.kit.Kit;

public class VoidKit extends Kit {
	
	public VoidKit() {
		super("Void", Material.RECORD_6, new ArrayList<ItemStack>(), 5000, "§7Todos que estão a 5 blocos de você serão", "§7puxados e levarão dano por 3 segundos.");
	}

}
