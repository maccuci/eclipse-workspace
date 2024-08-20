package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;

import me.tony.pvp.manager.kit.Kit;

public class SwitcherKit extends Kit {
	
	public SwitcherKit() {
		super("Switcher", Material.SNOW_BALL,new ArrayList<>(), 0, "§7Ao acertar a sua bola de neve em", "§7um inimigo troque de lugar com ele.");
	}

}
