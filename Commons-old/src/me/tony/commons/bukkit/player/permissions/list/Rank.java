package me.tony.commons.bukkit.player.permissions.list;

import java.util.HashMap;

import me.tony.commons.bukkit.player.tag.list.TagTypes;

public class Rank {
	
	private final HashMap<String, Boolean> permissions;

	private final String name;
	private final int id;
	private TagTypes tag;
	private long time;
	private boolean defaultRank, vip;

	public Rank(String name, int id, TagTypes tag, long time, boolean defaultRank) {
		this.name = name;
		this.id = id;
		this.tag = tag;
		this.time = time;
		this.defaultRank = defaultRank;
		this.permissions = new HashMap<>();
	}

	public void addPermission(String permission, Boolean active) {
		permissions.put(permission, active);
	}

	public String getName() {
		return name;
	}

	public HashMap<String, Boolean> getPermissions() {
		return permissions;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public int getId() {
		return id;
	}

	public TagTypes getTag() {
		return tag;
	}

	public boolean isDefaultRank() {
		return defaultRank;
	}

	public boolean isVip() {
		return vip;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setTagTypes(TagTypes tag) {
		this.tag = tag;
	}

	public void setDefaultRank(boolean defaultRank) {
		this.defaultRank = defaultRank;
	}

}
