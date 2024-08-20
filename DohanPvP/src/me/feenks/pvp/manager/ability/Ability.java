package me.feenks.pvp.manager.ability;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public class Ability {
	
	private String name, description;
	private Material icon;
	private ItemStack[] items;
	private int id;
	
	public Ability(String name, Material icon, String description, ItemStack... items) {
		this.name = name;
		this.icon = icon;
		this.items = items;
		this.description = description;
		this.id += 1;
	}

}
