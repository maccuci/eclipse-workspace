package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class UrgalKit extends Kit {
	
	public UrgalKit() {
		super("Urgal", "Efeitos", new ItemStack(Material.POTION, 1, (short) 8201), Group.BETA, "�7Ao iniciar da partida, ganhe 3 po��es de for�a.");
	}

}
