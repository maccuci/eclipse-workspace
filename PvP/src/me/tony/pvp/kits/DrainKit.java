package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;

import me.tony.pvp.manager.kit.Kit;

public class DrainKit extends Kit {
	
	public DrainKit() {
		super("Drain", Material.IRON_HOE, new ArrayList<>(), 2500, "§7Bate em um inimigo para roubar a vida dele.", "§7A vida roubada pode variar entre 2 a 4 corações.");
	}

}
