package com.fuzion.core.master.clan;

import java.util.List;

import lombok.Data;

@Data
public class Clan {
	
	private String owner, name, tag;
	private List<String> members;
	private int kills, deaths, xp, money;
	private ClanRank rank;
	
	public Clan(String name, String tag, String owner, String rank, List<String> members, int kills, int deaths, int xp, int money) {
		this.name = name;
		this.tag = tag;
		this.owner = owner;
		this.rank = ClanRank.valueOf(rank.toUpperCase());
		this.members = members;
		this.kills = kills;
		this.deaths = deaths;
		this.xp = xp;
		this.money = money;
	}

}
