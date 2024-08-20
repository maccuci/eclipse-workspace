package me.feenks.core.bukkit.player;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class AccountManager {
	
	private HashMap<UUID, Account> accounts = new HashMap<>(100);
	
	public Account craftAccount(Player player) {
		Account account = new Account(player.getUniqueId());
		account.load(account.getAtomicBoolean());
		
		if(accounts.containsKey(player.getUniqueId()))
			return null;
		accounts.put(player.getUniqueId(), account);
		return account;
	}
	
	public void unloadAccount(Player player) {
		Account account = getAccount(player);
		account.load(account.getAtomicBoolean());
		
		if(!accounts.containsKey(player.getUniqueId()))
			return;
		
		accounts.remove(player.getUniqueId(), account);
	}
	
	public Account getAccount(Player player) {
		return accounts.get(player.getUniqueId());
	}
}
