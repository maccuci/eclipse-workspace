package me.tony.commons.core.data.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Server {
	
	private int id, players;
	private String name, motd;
	private boolean online, whitelist;
	
	public Server(int id, String name, int players, boolean online) {
		this(id, name, " ", players, online);
	}
	
	public Server(int id, String name, String motd, int players, boolean online) {
		this.id = id;
		this.name = name;
		this.motd = motd;
		this.players = players;
		this.online = online;
	}
	
	public boolean canJoin() {
		return online == true || whitelist == false;
	}
	
	public void kill() {
		id = 0;
		name = "";
		motd = "";
		players = 0;
		online = false;
		setWhitelist(true);
	}
}
