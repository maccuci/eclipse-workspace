package me.tony.commons.core.data.management;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.event.server.ServerListPingEvent;

import me.tony.commons.bukkit.manager.CoreManager;
import me.tony.commons.bukkit.manager.management.Management;
import me.tony.commons.core.data.mysql.MySQL;

public class DataManager extends Management {
	
	private MySQL mySQL;
	
	public DataManager(CoreManager coreManager) {
		super(coreManager, "DataManager");
	}

	public boolean initialize() {
		return true;
	}
	
	public boolean openConnection() {
		try {
			mySQL = new MySQL();
			
			if (mySQL.openConnection()) {
				System.out.println("The primary and secondary connections have been established.");
			} else {
				System.out.println("Error the mysql connections have not been established.");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error when the mysql connections tries have not been established.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void handlerMotd(String server, ServerListPingEvent event) {
		if(server.contains("pvp")) {
			event.setMotd("                         §7[§f§lOlympus§4§lNetwork§7]       \n"
					+ "                     §aVenha treinar o seu PvP!");
		} else if(server == "hg-main") {
			event.setMotd("       §7[§f§lOlympus§4§lNetwork§7]       \n"
					+ "               §aEntre em uma sala de HG!");
		}
	}
	
	public MySQL getMySQL() {
		return mySQL;
	}
	
	public void playerExists(UUID uuid, AtomicBoolean atomicBoolean) {
		try {
			ResultSet resultSet = getMySQL().executeQuery("SELECT * FROM global_accounts WHERE uuid = " + uuid + ";");
			atomicBoolean.set(resultSet.next());
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
