package me.tony.commons.bukkit.player.account.type;

public enum StatsType {
	
	PVP,
	HG;
	
	private int serverId;
	
	private StatsType(int serverId) {
		this.serverId = serverId;
	}
	
	private StatsType() {
	}
	
	public int getServerId() {
		return serverId;
	}
}
