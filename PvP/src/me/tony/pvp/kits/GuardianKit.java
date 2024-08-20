package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;

import me.tony.pvp.manager.kit.Kit;

public class GuardianKit extends Kit {
	
	public GuardianKit() {
		super("Guardian", Material.ENDER_STONE, new ArrayList<>(), 3500, "§7Clique no item especial para ficar", "§7invencível durante 8 segundos.");
	}

}
