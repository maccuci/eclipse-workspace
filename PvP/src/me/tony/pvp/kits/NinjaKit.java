package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;

import me.tony.pvp.manager.kit.Kit;

public class NinjaKit extends Kit {
	
	public NinjaKit() {
		super("Ninja", Material.EMERALD, new ArrayList<>(), 3000, "§7Teleporte-se para o último inimigo hitado.");
	}

}
