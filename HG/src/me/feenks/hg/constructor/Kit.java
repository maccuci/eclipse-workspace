package me.feenks.hg.constructor;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

public abstract class Kit implements Listener {
	
	@Getter private String name, description;
	@Getter private Material iconMaterial;
	@Getter private short iconDurability;
	@Getter private List<ItemStack> items;
	
	public Kit(String name, String description, ItemStack icon, List<ItemStack> items) {
		this.name = name;
		this.description = description;
		this.iconMaterial = icon.getType();
		this.iconDurability = icon.getDurability();
		this.items = items;
	}
	
	public void addItem(ItemStack itemStack) {
		items.add(itemStack);
	}
	
	public ItemStack getIcon() {
		return new ItemStack(iconMaterial, 1, iconDurability);
	}
}
