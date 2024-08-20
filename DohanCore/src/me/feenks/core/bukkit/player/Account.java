package me.feenks.core.bukkit.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import lombok.Data;
import me.feenks.core.DohanAPI;
import me.feenks.core.bukkit.BukkitMain;
import me.feenks.core.master.data.permissions.group.GroupType;

@Data
public class Account {
	
	private final Map<String, Boolean> permissions = new HashMap<>();
	private AtomicBoolean atomicBoolean = new AtomicBoolean(true);
	
	private UUID uuid;
	private String nickname, lastip, ip;
	private Player player;
	
	private int coins, xp;
	
	private GroupType group;
	
	public Account(UUID uuid) {
		this.uuid = uuid;
	}
	
	public boolean load(AtomicBoolean load) {
		this.nickname = "";
		long start = System.currentTimeMillis();
		
		try {
			Connection connection = BukkitMain.getPlugin().getData().getMysql().getConnection();
			
			PreparedStatement query = connection.prepareStatement("SELECT * FROM `global_accounts` WHERE `uuid`=?");
			
			query.setString(1, uuid.toString());
			
			ResultSet data = query.executeQuery();
			
			if(data.next()) {
				DohanAPI.getLogger().log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] Getting the status of the player "
						+ uuid + "!");
				this.nickname = data.getString("nickname");
				this.lastip = data.getString("lastip");
				
				data.close();
				query.close();
			} else {
				this.coins = 0;
				this.xp = 0;
				this.group = GroupType.MEMBRO;
				
				String insert = "INSERT INTO `global_accounts` (`uuid`, `nickname`, `group`, `group_time`, `first_login`, `last_login`) VALUES (?, ?, ?, ?, ?, ?);";
				
				PreparedStatement accountInsert = connection.prepareStatement(insert);
				
				accountInsert.setString(1, uuid.toString());
				accountInsert.setString(2, nickname);
				accountInsert.setString(3, group.getName());
				accountInsert.setLong(4, System.currentTimeMillis() / 1000);
				accountInsert.execute();
				accountInsert.close();
				
				DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] The player " + uuid
						+ " have your account loaded with success!");
				if(load.get())
					atomicBoolean.set(true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			DohanAPI.log(Level.WARNING, "[" + (System.currentTimeMillis() - start)
					+ "ms] Error when the plugin tried to load the data of player with the uuid: " + uuid + ".",
					exception);
			atomicBoolean.set(true);
		}
		return this.atomicBoolean.get();
	}
	
	public void update() {
		
	}
	
	public void updatePermissions() {
		
	}
}
