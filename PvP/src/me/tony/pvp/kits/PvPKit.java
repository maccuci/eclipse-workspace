package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;

import me.tony.pvp.manager.kit.Kit;

public class PvPKit extends Kit {
	
	public PvPKit() {
		super("PvP", Material.STONE_SWORD, new ArrayList<>(), 0, "§7Venha com uma espada de pedra encantada.");
	}

}
