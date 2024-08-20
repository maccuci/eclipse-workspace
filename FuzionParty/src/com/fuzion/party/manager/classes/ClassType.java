package com.fuzion.party.manager.classes;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public abstract class ClassType implements Listener {
	
	private String name, description, minigame;
	private Material icon;
	
	public ClassType(String name, String minigame, String description, ItemStack icon) {
		this.name = name;
		this.minigame = minigame;
		this.description = description;
		this.icon = icon.getType();
	}

}
