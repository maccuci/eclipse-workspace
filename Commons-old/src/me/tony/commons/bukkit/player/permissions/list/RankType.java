package me.tony.commons.bukkit.player.permissions.list;

import me.tony.commons.bukkit.player.tag.list.TagTypes;

public enum RankType {
	
	OWNER(1, "Dono", TagTypes.OWNER),
	ADMINISTRATOR(2, "Admin", TagTypes.ADMINISTRATOR),
	MODERATOR_PLUS(3, "ModPlus", TagTypes.MODERATOR_PLUS),
	MODERATOR(4, "Mod", TagTypes.MODERATOR),
	TRIAL(5, "Trial", TagTypes.TRIAL),
	YOUTUBERPLUS(6, "YoutuberPlus", TagTypes.YOUTUBERPLUS),
	HELPER(7, "Ajudante", TagTypes.HELPER),
	BUILDER(8, "Builder", TagTypes.BUILDER),
	YOUTUBER(9, "Youtuber", TagTypes.YOUTUBER),
	BETA(10, "Beta", TagTypes.BETA),
	OLYMPUS(11, "Olympus", TagTypes.OLYMPUS),
	ARES(12, "Ares", TagTypes.ARES),
	HADES(13, "Hades", TagTypes.HADES),
	VIP(14, "Vip", TagTypes.VIP),
	NORMAL(15, "Normal", TagTypes.NORMAL);
	
	private String name;
	private TagTypes type;
	private int id;
	
	private RankType(int id, String name, TagTypes type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public TagTypes getType() {
		return type;
	}

}
