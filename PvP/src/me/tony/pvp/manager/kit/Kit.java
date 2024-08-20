package me.tony.pvp.manager.kit;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.tony.commons.api.cooldown.CooldownAPI;

public abstract class Kit implements Listener {
	
	private String name;
	private Material icon;
	private List<ItemStack> items;
	private String[] lore;
	private int price;

	public Kit(String name, Material icon, List<ItemStack> items, int price, String... lore) {
		this.name = name;
		this.icon = icon;
		this.items = items;
		this.lore = lore;
		this.price = price;
	}
	
	public void addCooldown(Player player, CooldownAPI cooldown) {
		cooldown.start();
	}
	
	public String getName() {
		return name;
	}
	
	public Material getIcon() {
		return icon;
	}
	
	public List<ItemStack> getItems() {
		return items;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String[] getLore() {
		return lore;
	}
}
