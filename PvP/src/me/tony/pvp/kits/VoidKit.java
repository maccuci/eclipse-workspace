package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.tony.pvp.manager.kit.Kit;

public class VoidKit extends Kit {
	
	public VoidKit() {
		super("Void", Material.RECORD_6, new ArrayList<ItemStack>(), 5000, "�7Todos que est�o a 5 blocos de voc� ser�o", "�7puxados e levar�o dano por 3 segundos.");
	}

}
