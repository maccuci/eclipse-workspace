package com.fuzion.party.manager.minegame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class PointManager {
	
	public static HashMap<Player, Integer> pointsMap = new HashMap<>();
	public static List<Player> test = new ArrayList<>(pointsMap.keySet());
	
	public static void addPoint(Player player, int point) {
		if(point <= 0)
			return;
		if(!pointsMap.containsKey(player)) {
			pointsMap.put(player, point);
		}
		pointsMap.put(player, pointsMap.get(player) + point);
	}
	
	public static int getPoints(Player player) {
		return pointsMap.get(player);
	}
	
	public static Player getPosition(Player player, int i) {
		return test.get(i);
	}
}
