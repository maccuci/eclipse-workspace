package me.tony.commons.core.account.punishments;

import java.util.UUID;

import me.tony.commons.core.account.punishments.consts.PunishConst;
import me.tony.commons.core.account.punishments.type.PunishType;

public class Ban extends PunishConst {

	public Ban(String punishedBy, UUID punishedByUuid, String punishedIp, PunishType timeType, String reason, long duration) {
		super(punishedBy, punishedByUuid, punishedIp, PunishType.BAN, timeType, reason, duration);
	}
	
}
