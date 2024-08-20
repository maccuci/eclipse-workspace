package com.fuzion.core.bukkit.player.account;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.player.group.Group;

public class AccountManager {
	
	private static final HashMap<Player, Account> accounts = new HashMap<>(100);
	
	public Account craftAccount(Player player) {
		Account account = new Account(player, player.getName(), player.getUniqueId(), 0, 0, Group.MEMBRO);
		long start = System.currentTimeMillis();
		
		account.create("groups");
		account.create("stats");
		accounts.put(player, account);
		System.out.println("[" + start + "ms] An account was created to " + account.getNickname() + "(" + account.getUuid() + ").");
		
		return account;
	}
	
	public Account getAccount(Player player) {
		return accounts.get(player);
	}
	
	public void unloadAccount(Player player) {
		Account account = getAccount(player);
		
		if(account != null)
			account.clear();
		accounts.remove(player);
	}
}
