package com.fuzion.core.master.account;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.fuzion.core.master.account.data.DataType;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.StatsManager;
import com.fuzion.core.master.data.language.Language;

import lombok.Getter;

@Getter
public class Account {
	
	
	private Player player;
	private UUID uniqueId;
	private String nickname;
	
	private int kills, deaths, coins, xp, wins;
	
	private Group group;
	private Language language;
	
	public Account(UUID uuid) {
		this.uniqueId = uuid;
	}
	
	public Account(Player player, UUID uuid, String nickname, int kills, int deaths, int coins, int xp, int wins) {
		this.uniqueId = uuid;
		this.nickname = nickname;
		this.player = player;
		this.kills = kills;
		this.deaths = deaths;
		this.coins = coins;
		this.xp = xp;
		this.wins = wins;
		this.language = Language.PORTUGUESE;
		this.group = Group.NORMAL;
	}
	
	public Account setXp(int xp) {
		this.xp += xp;
		new StatsManager().set(uniqueId, DataType.GLOBAL_XP.name().replace("GLOBAL_", "").toLowerCase(), xp);
		return this;
	}
	
	public void addXp(int xp) {
		if(xp <= 0)
			return;
		if(getXp() >= 5000000)
			return;
		setXp(xp);
	}
	
	public Account setGroup(Group group) {
		this.group = group;
		new GroupManager().set(uniqueId, DataType.GLOBAL_PLAYERGROUP.name().replace("GLOBAL_", "").toLowerCase(), group.name());
		return this;
	}
	
	public Account setGroup(Group group, long expire) {
		this.group = group;
		new GroupManager().set(uniqueId, DataType.GLOBAL_PLAYERGROUP.name().replace("GLOBAL_", "").toLowerCase(), group.name());
		new GroupManager().set(uniqueId, DataType.GLOBAL_TEMPORARY.name().replace("GLOBAL_", "").toLowerCase(), 1);
		new GroupManager().set(uniqueId, DataType.GLOBAL_EXPIRE.name().replace("GLOBAL_", "").toLowerCase(), expire);
		return this;
	}
	
	public Account setLanguage(Language language) {
		this.language = language;
		return this;
	}
}
