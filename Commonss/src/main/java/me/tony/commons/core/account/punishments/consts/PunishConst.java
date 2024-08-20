package me.tony.commons.core.account.punishments.consts;

import java.util.UUID;

import lombok.Getter;
import me.tony.commons.core.account.punishments.type.PunishType;

@Getter
public class PunishConst {
	
	private String punishedBy;
	private UUID punishedByUniqueId;
	private String punishedIp;
	
	private PunishType punishTimeType, punishType;
	
	private long punishTime;
	private String reason;
	
	private long expire, duration;
	
	public PunishConst(String punishedBy, UUID punishedByUuid, String punishedIp, PunishType type, String reason) {
		this.punishedBy = punishedBy;
		this.punishedByUniqueId = punishedByUuid;
		this.punishedIp = punishedIp;
		this.punishType = type;
		this.punishTimeType = PunishType.PERMANENT;
		this.reason = reason;
		this.duration = -1;
	}
	
	public PunishConst(String punishedBy, UUID punishedByUuid, String punishedIp, PunishType type, PunishType timeType, String reason, long duration) {
		this.punishedBy = punishedBy;
		this.punishedByUniqueId = punishedByUuid;
		this.punishedIp = punishedIp;
		this.punishType = type;
		this.punishTimeType = timeType;
		this.reason = reason;
		this.duration = duration;
	}
	
	public boolean hasExpired() {
		return expire != - 1 && expire < System.currentTimeMillis();
	}
	
	public boolean isPermanent() {
		return expire == -1;
	}
}
