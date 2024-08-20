package me.ale.commons.core.account;

import lombok.Getter;

@Getter
public enum Tag {
	
	OWNER("§4§l", Rank.OWNER),
	ADMIN("§c§l", Rank.ADMIN),
	MOD("§5§l", Rank.MOD),
	TRIAL("§d§l", Rank.TRIAL),
	HELPER("§9§l", Rank.HELPER),
	YOUTUBER("§b§l", Rank.YOUTUBER),
	FRIEND("§e§l", Rank.FRIEND),
	SIMPLE("§a§l", Rank.SIMPLE),
	NORMAL("§7§l", Rank.NORMAL);
	
	private String color;
	private Rank rank;
	
	private Tag(String color, Rank rank) {
		this.color = color;
		this.rank = rank;
	}

}
