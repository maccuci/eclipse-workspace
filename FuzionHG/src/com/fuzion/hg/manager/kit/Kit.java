package com.fuzion.hg.manager.kit;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.hg.Main;

import lombok.Getter;

@Getter
public abstract class Kit implements Listener {
	
	private String name, explanation, type;
	private Material icon;
	private int durability;
	private Group group;
	public transient Set<ItemStack> items = new HashSet<>();
	
	public Kit(String name, String type, ItemStack icon, Group group, String explanation) {
		this.name = name;
		this.type = type;
		this.icon = icon.getType();
		this.durability = icon.getDurability();
		this.group = group;
		this.explanation = explanation;
	}
	
	public boolean hasKit(Player player) {
		return new KitManager().getKitName(player) == getName();
	}
	
	public boolean canUseKit(Player player) {
		GroupManager manager = new GroupManager();
		return manager.hasGroupPermission(player.getUniqueId(), group) || KitMatch.hasKitMatch(player, getName()) || Main.getPlugin().getWinnerManager().hasWinner(player) || new KitManager().hasSQLKit(player.getUniqueId(), getName());
	}
	
	public void addItem(ItemStack itemStack) {
		if(items.contains(itemStack))
			return;
		items.add(itemStack);
	}
	
	public boolean isKitItem(Kit kit, ItemStack item) {
		for (ItemStack it : items) {
			if(ItemBuilder.isEquals(item, it))
				return true;
		}
		return false;
	}
}
