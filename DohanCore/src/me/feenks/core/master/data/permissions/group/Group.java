package me.feenks.core.master.data.permissions.group;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Group {
	
	private final Map<String, Boolean> permissions;
	
	private String name;
	private int id;
	private long time;
	private boolean defaultGroup, vip;
	
	public Group(String name, int id, long time, boolean defaultGroup) {
		this.name = name;
		this.id = id;
		//this.tag = tag;
		this.time = time;
		this.defaultGroup = defaultGroup;
		this.permissions = new HashMap<>();
	}
	
	public void addPermission(String permission, Boolean active) {
		permissions.put(permission, active);
	}
}
