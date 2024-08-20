package br.com.houldmc.rankup.manager.rank.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;

@Getter
public enum RankList {
	
	EMPEROR("Emperor", "§4Imperador", 0),
	EMPEROR_I("Emperor I", "§4Imperador I", 0),
	EMPEROR_II("Emperor II", "§4Imperador II", 0),
	EMPEROR_III("Emperor III", "§4Imperador III", 0),
	DUKE("Duke", "§2Duque", 0),
	DUKE_I("Duke I", "§2Duque I", 0),
	DUKE_II("Duke II", "§2Duque II", 0),
	DUKE_III("Duke III", "§2Duque III", 0),
	NOBLE("Noble", "§6Nobre", 0),
	NOBLE_I("Noble I", "§6Nobre I", 0),
	NOBLE_II("Noble II", "§6Nobre II", 0),
	NOBLE_III("Noble III", "§6Nobre III", 0),
	KNIGHT("Knight", "§cKnight", 0),
	KNIGHT_I("Knight I", "§cKnight I", 0),
	KNIGHT_II("Knight II", "§cKnight II", 0),
	KNIGHT_III("Knight III", "§cKnight III", 0),
	CLERGY("Clergy", "§fClero", 0),
	CLERGY_I("Clergy I", "§fClero I", 0),
	CLERGY_II("Clergy II", "§fClero II", 0),
	CLERGY_III("Clergy III", "§fClero III", 0),
	ARTISAN("Artisan", "§eArtesão", 0),
	ARTISAN_I("Artisan I", "§eArtesão I", 0),
	ARTISAN_II("Artisan II", "§eArtesão II", 0),
	ARTISAN_III("Artisan III", "§eArtesão III", 0),
	FARMER("Farmer", "§7Camponês", 0),
	FARMER_I("Farmer I", "§7Camponês I", 0),
	FARMER_II("Farmer II", "§7Camponês II", 0),
	FARMER_III("Farmer III", "§7Camponês III", 0);
	
	private String tag, name;
	private double price;
	
	private RankList(String name, String tag, double price) {
		this.tag = tag;
		this.name = name;
		this.price = price;
	}
	
	public static void sortByOrder(int idOrder) {
		List<RankList> ranks = new ArrayList<>();
		
		if(idOrder == 1) {
			for(RankList rank : values()) {
				ranks.add(rank);
				Collections.sort(ranks, new Comparator<RankList>() {

					@Override
					public int compare(RankList o1, RankList o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
			}
		} else if(idOrder == 2) {
			for(RankList rank : values()) {
				ranks.add(rank);
				Collections.sort(ranks, new Comparator<RankList>() {

					@Override
					public int compare(RankList o1, RankList o2) {
						return o2.getName().compareTo(o1.getName());
					}
				});
			}
		}
	}
}
