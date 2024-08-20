package me.tony.commons.bukkit.player.account;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class AccountManager {
	
	private static final Map<UUID, Account> accounts = new HashMap<>();
	
	public Account craftAccount(Player player) {
		Account account = new Account(player);
		accounts.put(player.getUniqueId(), account);
		return account;
	}
	
	public Account getAccount(Player player) {
		return accounts.get(player.getUniqueId());
	}
	
	public void unloadAccount(Player player) {
		Account account = getAccount(player);

		accounts.remove(player.getUniqueId(), account);
	}

	public Map<UUID, Account> getAccounts() {
		return accounts;
	}
}
