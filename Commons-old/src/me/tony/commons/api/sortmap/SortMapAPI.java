package me.tony.commons.api.sortmap;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import me.tony.commons.bukkit.player.account.Account;

public class SortMapAPI {

	private static final Map<String, Integer> unordered = new HashMap<String, Integer>();
	private static final Map<String, Integer> ordered = new HashMap<String, Integer>();

	public static void addPlayerToSort(String playerName, int scores) {
		unordered.put(playerName, scores);
	}
	
	public Map<String, Integer> sortMapBySimpleMethod() {
		String playername;
		int highestvalue = 0;
		for (int i = 0; i < unordered.size(); i++) {
			for (Entry<String, Integer> e : unordered.entrySet()) {
				if (e.getValue() > highestvalue) {
					highestvalue = e.getValue();
					playername = e.getKey();
					ordered.put(playername, highestvalue);
					unordered.remove(playername);
				}
			}
		}
		return ordered;
	}

	public Map<Account, Integer> sortMapComparator(Map<Account, Integer> unsortMap, boolean order) {
		List<Entry<Account, Integer>> list = new LinkedList<Entry<Account, Integer>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Entry<Account, Integer>>() {
			public int compare(Entry<Account, Integer> o1, Entry<Account, Integer> o2) {
				return order ? o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue());
			}
		});

		Map<Account, Integer> sortedMap = new LinkedHashMap<Account, Integer>();

		for (Entry<Account, Integer> entry : list)
			sortedMap.put(entry.getKey(), entry.getValue());

		return sortedMap;
	}

	public void display(Map<Account, Integer> map, int topers, boolean broadcast) {
		sortMapComparator(map, true);
		String playerName;
		int scores;

		if (topers <= 2) {
			System.out.println("For list top, need number big of 2");
			return;
		}

		if (topers == 3) {
			for (int i = 0; 1 < 3; i++) {
				for (Entry<Account, Integer> e : map.entrySet()) {
					playerName = e.getKey().getPlayer().getName();
					scores = e.getValue();

					if (broadcast)
						Bukkit.broadcastMessage("§6Colocações: \n " + i + "° " + playerName + ": " + scores
								+ (scores > 1 ? "ponto" : "pontos"));
				}
			}
		} else if (topers == 4) {
			for (int i = 0; 1 < 4; i++) {
				for (Entry<Account, Integer> e : map.entrySet()) {
					playerName = e.getKey().getPlayer().getName();
					scores = e.getValue();

					if (broadcast)
						Bukkit.broadcastMessage("§6Colocações: \n " + i + "° " + playerName + ": " + scores
								+ (scores > 1 ? "ponto" : "pontos"));
				}
			}
		} else if (topers == 5) {
			for (int i = 0; 1 < 5; i++) {
				for (Entry<Account, Integer> e : map.entrySet()) {
					playerName = e.getKey().getPlayer().getName();
					scores = e.getValue();

					if (broadcast)
						Bukkit.broadcastMessage("§6Colocações: \n " + i + "° " + playerName + ": " + scores
								+ (scores > 1 ? "ponto" : "pontos"));
				}
			}
		} else {
			for (int i = 0; 1 < map.size(); i++) {
				for (Entry<Account, Integer> e : map.entrySet()) {
					playerName = e.getKey().getPlayer().getName();
					scores = e.getValue();

					if (broadcast)
						Bukkit.broadcastMessage("§6Colocações: \n " + i + "° " + playerName + ": " + scores
								+ (scores > 1 ? "ponto" : "pontos"));
				}
			}
		}
	}

	public void display(int topers, boolean broadcast) {
		sortMapBySimpleMethod();
		String playerName;
		int scores;

		if (topers <= 2) {
			System.out.println("For list top, need number big of 2");
			return;
		}

		if (topers == 3) {
			for (int i = 0; 1 < 3; i++) {
				for (Entry<String, Integer> e : ordered.entrySet()) {
					playerName = e.getKey();
					scores = e.getValue();

					if (broadcast)
						Bukkit.broadcastMessage("§6Colocações: \n " + i + "° " + playerName + ": " + scores
								+ (scores > 1 ? "ponto" : "pontos"));
				}
			}
		} else if (topers == 4) {
			for (int i = 0; 1 < 4; i++) {
				for (Entry<String, Integer> e : ordered.entrySet()) {
					playerName = e.getKey();
					scores = e.getValue();

					if (broadcast)
						Bukkit.broadcastMessage("§6Colocações: \n " + i + "° " + playerName + ": " + scores
								+ (scores > 1 ? "ponto" : "pontos"));
				}
			}
		} else if (topers == 5) {
			for (int i = 0; 1 < 5; i++) {
				for (Entry<String, Integer> e : ordered.entrySet()) {
					playerName = e.getKey();
					scores = e.getValue();

					if (broadcast)
						Bukkit.broadcastMessage("§6Colocações: \n " + i + "° " + playerName + ": " + scores
								+ (scores > 1 ? "ponto" : "pontos"));
				}
			}
		} else {
			for (int i = 0; 1 < ordered.size(); i++) {
				for (Entry<String, Integer> e : ordered.entrySet()) {
					playerName = e.getKey();
					scores = e.getValue();

					if (broadcast)
						Bukkit.broadcastMessage("§6Colocações: \n " + i + "° " + playerName + ": " + scores
								+ (scores > 1 ? "ponto" : "pontos"));
				}
			}
		}
	}
}
