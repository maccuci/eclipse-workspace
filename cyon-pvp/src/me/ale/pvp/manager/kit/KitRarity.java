package me.ale.pvp.manager.kit;

import lombok.Getter;

@Getter
public enum KitRarity {
	
	EXTRAORDINARY("§6"),
	MYSTIC("§c"),
	RARE("§9"),
	COMMON("§f");
	
	private String rarityColor;
	
	private KitRarity(String rarityColor) {
		this.rarityColor = rarityColor;
	}

}
