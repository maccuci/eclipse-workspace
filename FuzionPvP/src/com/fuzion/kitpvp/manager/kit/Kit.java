package com.fuzion.kitpvp.manager.kit;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;

import lombok.Getter;

@Getter
public abstract class Kit implements Listener {
	
	private String name, explanation;
	private Group group;
	private Material icon;
	private ItemStack item;
	private boolean released;
	public transient Set<ItemStack> items = new HashSet<>();
	
	public Kit(String name, ItemStack item, Group group, boolean released, String explanation) {
		this.name = name;
		this.item = item;
		icon = item.getType();
		this.released = released;
		this.group = group;
		this.explanation = explanation;
	}
	
	public Kit(String name, ItemStack item, Group group, String explanation) {
		this.name = name;
		this.item = item;
		icon = item.getType();
		this.released = false;
		this.group = group;
		this.explanation = explanation;
	}
	
	public boolean hasKit(Player player) {
		return new KitManager().getKitName(player) == getName();
	}
	
	public void addItem(ItemStack itemStack) {
		if(items.contains(itemStack))
			return;
		items.add(itemStack);
	}
	
	public boolean canUseKit(Player player) {
		GroupManager manager = new GroupManager();
		return manager.hasGroupPermission(player.getUniqueId(), group) || new KitManager().hasSQLKit(player.getUniqueId(), getName());
	}
}
