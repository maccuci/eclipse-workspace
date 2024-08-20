package com.fuzion.core.master.clan;

import lombok.Getter;

@Getter
public enum ClanRank {
	
	INITIAL("§8", 0, "-"),
	PRATA("§7", 1000, "✦"),
	OURO("§6", 2000, "✽"),
	DIAMANTE("§b", 3000, "✸"),
	ESMERALDA("§2", 4000, "�?�"),
	RUBY("§c", 5000, "✤"),
	SAFIRA("§3", 6000, "�?�"),
	MASTER("§4", 7000, "☯");
	
	private String color, symbol;
	private int xp;
	
	private ClanRank(String color, int xp, String symbol) {
		this.color = color;
		this.xp = xp;
		this.symbol = symbol;
	}

}
