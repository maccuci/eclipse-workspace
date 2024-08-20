package me.tony.commons.bukkit.player.permissions;

public class Permissible {
	
	private final String name;
	private final boolean active;
	private final long time;

	public Permissible(String name, boolean active, long time) {
		this.name = name;
		this.active = active;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public long getTime() {
		return time;
	}

}
