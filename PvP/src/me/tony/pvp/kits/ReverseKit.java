package me.tony.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;

import me.tony.pvp.manager.kit.Kit;

public class ReverseKit extends Kit {
	
	public ReverseKit() {
		super("Reverse", Material.DRAGON_EGG, new ArrayList<>(), 4000, "§7Roube a velocidade de todos em sua volta", "§7no raio de 5 blocos.");
	}

}
