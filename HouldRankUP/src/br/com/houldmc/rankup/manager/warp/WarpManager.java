package br.com.houldmc.rankup.manager.warp;

import java.util.HashMap;
import java.util.Map;

import br.com.houldmc.rankup.manager.warp.list.WarpList;

public class WarpManager {
	
	private static final Map<String, WarpList> warps = new HashMap<>();
	
	public void createWarp(String name, WarpList warp) {
		warps.put(name, warp);
	}
	
	public WarpList getWarp(String name) {
		return warps.get(name);
	}
}
