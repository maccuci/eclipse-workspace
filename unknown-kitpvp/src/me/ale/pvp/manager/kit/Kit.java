package me.ale.pvp.manager.kit;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import lombok.Data;
import lombok.NonNull;

@Data
@NonNull
public abstract class Kit implements Listener {
	
	private String name;
	private Material icon;
	private ItemStack[] items;
	private String[] lore;
	
	public Kit(String name, Material icon, ItemStack[] items, String... lore) {
		this.name = name;
		this.icon = icon;
		this.items = items;
		this.lore = lore;
	}

}
