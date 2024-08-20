package me.tony.commons.bukkit.player.account;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.tony.commons.bukkit.BukkitMain;
import me.tony.commons.bukkit.player.account.crud.ICRUD;

public class AccountCRUD implements ICRUD<Account, UUID> {

	Connection connection = BukkitMain.getPlugin(BukkitMain.class).getCoreManager().getDataManager().getMySQL().getConnection();
	
	@Override
	public void create(Account object) {
		try {
			boolean accountCreate = connection.createStatement().execute("INSERT INTO `global_accounts` (uniqueId, nick, group, group_time, first_login, last_login, ip) VALUES ('" + object.getUniqueId() + "', '" + object.getNickname() + "', '" + "NORMAL" +"', '" + 0L + "', '"+ new Date() + "', '" + new Date() + "', '" + object.getPlayer().getAddress().getHostString() + "');");
			
			if(accountCreate) 
				System.out.println("New player account " + object.getNickname() + "(" + object.getUniqueId() + ") was loaded.");
			else
				System.out.println("Failed the load account.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Account read(UUID object) {
		try {
			PreparedStatement accountQuery = connection.prepareStatement("SELECT * FROM global_accounts WHERE `uniqueId`=?");
			accountQuery.setString(1, object.toString());
			
			ResultSet accountData = accountQuery.executeQuery();
			
			if(accountData.next()) {
				Account account = new Account(Bukkit.getPlayer(object));
				
				account.setNickname(accountData.getString("nick"));
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public void update(Account object) {
		
	}

	@Override
	public void delete(UUID object) {

	}
}
