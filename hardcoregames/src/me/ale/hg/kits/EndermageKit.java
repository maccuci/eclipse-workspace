package me.ale.hg.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.manager.kit.Kit;

public class EndermageKit extends Kit {
	
	public EndermageKit() {
		super("Endermage", Material.ENDER_PORTAL_FRAME, new ItemStack(Material.ENDER_PORTAL_FRAME), "§7Puxe os jogadores que estiverem no mesmo raio que deu portal.", 4);
	}

}
