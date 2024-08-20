package br.com.houldmc.rankup.player.punish;

import java.util.Date;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PunishRecord {
	
	private int id;
	private Player punished, staff;
	private long start, expire;
	private String motive;
	private boolean active;
	private PunishType type;
	
	public PunishRecord(int id) {
		this.id = id;
	}
	
	public PunishRecord(Player punished) {
		this.punished = punished;
	}
	
	public PunishRecord(int id, Player punished, Player staff, long start, long expire, String motive, boolean active, PunishType type) {
		this.id = id;
		this.punished = punished;
		this.staff = staff;
		this.start = start;
		this.expire = expire;
		this.motive = motive;
		this.active = active;
		this.type = type;
	}
	
	public boolean isActive() {
		if (active && expire <= 10000) {
			return true;
		} else if (active) {
			if (new Date().before(new Date(expire)))
				return true;
			if (new Date().after(new Date(expire))) {
				new PunishManager().unPunishPlayer(punished.getUniqueId());
				active = false;
			}
			return true;
		} else if (!active) {
			return false;
		} else {
			return false;
		}
	}
}
