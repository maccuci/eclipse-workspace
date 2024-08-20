package br.com.houldmc.rankup.player.clan.utilitaries;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Clan {
	
	private UUID owner;
	private String name, tag;
	private List<String> members;
	private int money;
	
	public Clan(UUID owner, String name, String tag, List<String> members, int money) {
		this.owner = owner;
		this.name = name;
		this.tag = tag;
		this.members = members;
		this.money = money;
	}
}
