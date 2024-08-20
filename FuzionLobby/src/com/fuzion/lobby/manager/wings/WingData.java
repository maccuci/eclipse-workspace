package com.fuzion.lobby.manager.wings;

import org.bukkit.entity.Player;

public class WingData {

	public int wingType;
	public float currentSize;
	public boolean equipped;
	public boolean enableGlide;

	public WingData(Player player, int wingType) {
		this.wingType = wingType;
		this.currentSize = 0f;
		this.equipped = true;
	}

	public void toggleEquip() {
		this.equipped = !this.equipped;
	}

}
