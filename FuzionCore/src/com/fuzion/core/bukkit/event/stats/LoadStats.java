package com.fuzion.core.bukkit.event.stats;

import lombok.Getter;

@Getter
public enum LoadStats {
	
	SUCESS(1),
	FAILED(2);
	
	private int id;
	
	private LoadStats(int id) {
		this.id = id;
	}
	
	public String toString() {
		return name().toString();
	}
}
