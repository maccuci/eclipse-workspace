package com.fuzion.core.master.account.data;

import java.util.Arrays;

public enum DataType {
	
	PVP_KILLS(1),
	PVP_DEATHS(2),
	PVP_STREAK(3),
	HG_WINS(4),
	GLOBAL_XP(5),
	GLOBAL_COINS(6),
	GLOBAL_CRATES(7),
	GLOBAL_PLAYERGROUP(8),
	GLOBAL_TEMPORARY(9),
	GLOBAL_EXPIRE(10);
	
	private final int id;
	
	private DataType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static DataType getDataType(int id) {
		return Arrays.asList(values()).stream().filter(data -> data.id == id).findFirst().orElse(null);
	}
}
