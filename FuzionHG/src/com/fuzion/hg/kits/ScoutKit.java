package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class ScoutKit extends Kit {
	
	public ScoutKit() {
		super("Scout", "Efeitos", new ItemStack(Material.POTION, 1, (short) 8194), Group.BETA, "§7Ao iniciar da partida, ganhe 3 poções de velocidade.");
	}

}
