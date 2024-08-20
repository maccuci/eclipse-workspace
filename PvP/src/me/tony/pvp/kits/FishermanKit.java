package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;

import me.tony.pvp.manager.kit.Kit;

public class FishermanKit extends Kit {
	
	public FishermanKit() {
		super("Fisherman", Material.FISHING_ROD, new ArrayList<>(), 500, "§7Com a sua vara de pescar", "§7puxe os inimigos até você.");
	}

}
