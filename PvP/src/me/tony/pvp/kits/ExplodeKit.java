package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;

import me.tony.pvp.manager.kit.Kit;

public class ExplodeKit extends Kit {
	
	public ExplodeKit() {
		super("Explode", Material.TNT, new ArrayList<>(), 2000, "§7Crie uma explosão causando 4 de dano.");
	}
	
}
