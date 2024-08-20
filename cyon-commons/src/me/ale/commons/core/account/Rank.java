package me.ale.commons.core.account;

import lombok.Getter;

@Getter
public enum Rank {
	
	OWNER("§4", Tag.OWNER, "*"),
	ADMIN("§c", Tag.ADMIN, ""),
	MOD("§5", Tag.MOD, ""),
	TRIAL("§d", Tag.TRIAL, ""),
	HELPER("§9", Tag.HELPER, ""),
	YOUTUBER("§b", Tag.YOUTUBER, ""),
	FRIEND("§e", Tag.FRIEND, ""),
	SIMPLE("§a", Tag.SIMPLE, ""),
	NORMAL("§7", Tag.NORMAL, "");
	
	private String color;
	private Tag tag;
	private String[] permissions;
	
	private Rank(String color, Tag tag, String... permissions) {
		this.color = color;
		this.tag = tag;
		this.permissions = permissions;
	}

}
