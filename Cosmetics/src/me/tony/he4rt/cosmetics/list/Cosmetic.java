package me.tony.he4rt.cosmetics.list;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cosmetic {
	
	private Player owner;
	private String name;
	private Category category;
	private Material icon;
	private String[] description;
	
	public Cosmetic() {
	}
	
	public Cosmetic(Player owner, String name, Material icon, Category category, String... description) {
		this.owner = owner;
		this.name = name;
		this.icon = icon;
		this.category = category;
		this.description = description;
	}
	
}
