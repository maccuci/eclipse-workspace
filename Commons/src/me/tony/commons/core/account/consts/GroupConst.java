package me.tony.commons.core.account.consts;

import java.util.HashMap;

import lombok.Getter;

@Getter
public class GroupConst {
	
	private final HashMap<String, Boolean> permissions;
	
	private String name;
	private int id;
	private boolean defaultRank, vip;
	
	public GroupConst(int id, String name) {
		this.id = id;
		this.name = name;
		this.permissions = new HashMap<>();
	}
	
	public void addPermission(String permission, Boolean active) {
		permissions.put(permission, active);
	}
	
	public void setVip(boolean vip) {
		this.vip = vip;
	}
	
	public void setDefaultRank(boolean defaultRank) {
		this.defaultRank = defaultRank;
	}
}
