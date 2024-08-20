package me.tony.commons.core;

import java.util.UUID;

import org.bukkit.Bukkit;

import lombok.Getter;
import me.tony.commons.core.account.AccountCommon;
import me.tony.commons.core.account.CommonsPlayer;
import me.tony.commons.core.data.backend.mysql.MySQL;
import me.tony.commons.core.data.logger.CommonLogger;
import me.tony.commons.core.managements.GroupCommon;
import me.tony.commons.core.managements.TagCommon;

public class Commons {
	
	@Getter
	private static AccountCommon accountCommon = new AccountCommon();
	
	@Getter
	private static TagCommon tagCommon = new TagCommon();
	
	@Getter
	private static GroupCommon groupCommon = new GroupCommon();
	
	@Getter
	private static CommonLogger commonLogger = new CommonLogger(Bukkit.getLogger(), "Common", true);
	
	public static CommonsPlayer getAccount(UUID uuid) {
		return accountCommon.getAccount(uuid);
	}
	
	@Getter
	private static MySQL mySQL = new MySQL();
	
}
