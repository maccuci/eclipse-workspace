package br.com.houldmc.rankup.manager.kit;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public abstract class Kit {
	
	private String name;
	private List<ItemStack> items;
	
	public Kit(String name, List<ItemStack> items) {
		this.name = name;
		this.items = items;
	}
	
	public abstract void givePlayer(Player player);
}
