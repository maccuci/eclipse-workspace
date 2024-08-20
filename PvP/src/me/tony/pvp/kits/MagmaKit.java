package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;

import me.tony.pvp.manager.kit.Kit;

public class MagmaKit extends Kit {
	
	public MagmaKit() {
		super("Magma", Material.LAVA_BUCKET, new ArrayList<>(), 500, "§7Deixe seus inimigos pegando fogo ao te tocarem.");
	}

}
