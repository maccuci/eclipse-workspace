package com.fuzion.kitpvp.manager.warp;

import lombok.Getter;

@Getter
public enum Warps {
	
	NONE("Spawn", false),
	SPAWN("Spawn", false),
	FPS("FPS", false),
	LAVA("Lava Challenge", false),
	ONE_VS_ONE("Spawn", true);
	
	private String name;
	private boolean status;

	private Warps(String name, boolean status) {
		this.name = name;
		this.status = status;
	}
	
	public static boolean contains(String warp) {
		for (Warps warps : Warps.values()) {
			if (warp.equalsIgnoreCase(warps.toString()))
				return true;
		}
		if (warp.contains("simulator"))
			return true;

		return false;
	}

	public static Warps getFromString(String warpName) {
		for (Warps warps : Warps.values()) {
			if (warps.getName().equalsIgnoreCase(warpName))
				return warps;
		}
		
		warpName = warpName.toLowerCase();
		
		if (warpName.contains("arena"))
			return Warps.SPAWN;

		if (warpName.contains("1v1"))
			return Warps.ONE_VS_ONE;

		if (warpName.contains("lava") || warpName.contains("challenge"))
			return Warps.LAVA;
		
		return null;
	}

}
