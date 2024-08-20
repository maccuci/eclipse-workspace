package me.tony.commons.core.account;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.Player;

import lombok.Getter;

@Getter
public class CommonsPlayer {
	
	private Player player;
	private String name;
	private UUID uuid;
	
	private Group group;
	
	private int coins, xp;
	
	private transient String ipAddress = "";
	private String lastIpAddress = "";
	
	private boolean online;
	
	
	public CommonsPlayer(Player player, String name) {
		this.player = player;
		this.uuid = player.getUniqueId();
		this.name = name;
	}
	
	public boolean load(AtomicBoolean atomicBoolean) {
		return atomicBoolean.get();
	}
	
	public boolean update() {
		return false;
	}
}
