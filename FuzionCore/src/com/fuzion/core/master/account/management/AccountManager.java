package com.fuzion.core.master.account.management;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.fuzion.core.master.account.Account;

public class AccountManager {
	
	private static final HashMap<UUID, Account> accounts = new HashMap<>(200);
	
	public static Account craftAccount(Player player) {
		Account account = new Account(player.getUniqueId());
		accounts.put(player.getUniqueId(), account);
		return account;
	}
	
	public static Account getAccount(Player player) {
		return accounts.get(player.getUniqueId());
	}
	
	public static Account getAccount(UUID uuid) {
		return accounts.get(uuid);
	}

}
