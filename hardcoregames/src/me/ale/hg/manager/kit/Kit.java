package me.ale.hg.manager.kit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public abstract class Kit {
	
	private String name;
	private Material icon;
	private ItemStack items;
	private String lore;
	private int points;
    
	public Kit(String name, Material icon, ItemStack items, String lore, int points) {
		this.name = name;
		this.icon = icon;
		this.items = items;
		this.lore = lore;
		this.points = points;
	}
	
    public boolean hasKit(Player player) {
    	return new KitManager().getKitName(player) == name;
    }
}
