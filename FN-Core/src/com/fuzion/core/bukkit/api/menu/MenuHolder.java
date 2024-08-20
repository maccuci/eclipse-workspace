package com.fuzion.core.bukkit.api.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuHolder implements InventoryHolder {
	
	private MenuInventory menu;
	  
	public MenuHolder(MenuInventory menuInventory) {
		this.menu = menuInventory;
	}

	public MenuInventory getMenu() {
		return this.menu;
	}

	public void setMenu(MenuInventory menu) {
		this.menu = menu;
	}

	public boolean isOnePerPlayer() {
		return this.menu.isOnePerPlayer();
	}

	public void destroy() {
		this.menu = null;
	}

	public Inventory getInventory() {
		if (isOnePerPlayer()) {
			return null;
		}
		return this.menu.getInventory();
	}

}
