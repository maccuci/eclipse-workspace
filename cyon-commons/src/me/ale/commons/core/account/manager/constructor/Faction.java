package me.ale.commons.core.account.manager.constructor;

import java.util.List;

import lombok.Data;

@Data
public class Faction {
	
	private String name, owner, message;
	private List<String> members;
	private int kills, deaths, money;
	
	public Faction(String name, String owner, String message, List<String> members, int kills, int deaths, int money) {
		this.name = name;
		this.owner = owner;
		this.message = message;
		this.members = members;
		this.kills = kills;
		this.deaths = deaths;
		this.money = money;
	}
}
