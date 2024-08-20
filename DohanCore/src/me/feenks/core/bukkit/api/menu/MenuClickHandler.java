package me.feenks.core.bukkit.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface MenuClickHandler {
	
	public abstract void onClick(Player paramPlayer, Inventory paramInventory, ClickType paramClickType, ItemStack paramItemStack, int paramInt);

}
