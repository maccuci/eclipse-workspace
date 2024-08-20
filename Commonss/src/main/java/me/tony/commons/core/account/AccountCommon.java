package me.tony.commons.core.account;

import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;

public class AccountCommon {
	
	@Getter private static final HashMap<UUID, CommonsPlayer> accounts = new HashMap<>(100);
	
	public CommonsPlayer loadCommonsPlayer(CommonsPlayer account) {
		accounts.put(account.getUuid(), account);
		
		return account;
	}
	
	public void unloadCommonsPlayer(UUID uuid) {
		CommonsPlayer player = getAccount(uuid);
		
		if(player == null) {
			new Exception("The account don't find. Try other.");
			return;
		}
		
		accounts.remove(uuid, player);
	}
	
	public CommonsPlayer getAccount(UUID uuid) {
		return accounts.get(uuid);
	}
}
