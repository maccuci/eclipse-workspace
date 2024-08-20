package com.fuzion.core.master.account.management;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.BukkitMain;

public class KitFavoritesManager {
	
	public static List<String> kitsFavoritos = new ArrayList<>();
	
	public void load(Player player) {
		try {
			PreparedStatement statement = BukkitMain.getPlugin().getMysqlBackend().prepareStatment(
					"SELECT * FROM `kitsfavs` WHERE (`uuid` ='" + player.getUniqueId().toString() + "');");
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				List<String> kitsFavs = new ArrayList<String>();
				String kits = result.getString("kits");
				if (kits.contains(",")) {
					String[] split;
					for (int length = (split = kits.split(",")).length, i = 0; i < length; ++i) {
						final String str = split[i];
						kitsFavs.add(str);
					}
				} else {
					kitsFavs.add(kits);
				}
				statement.close();
				result.close();
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addKitFavorite(Player player, String name) {
			if (!kitsFavoritos.contains(name)) {
				kitsFavoritos.add(name);
				updateFunction(player, name, "add");
			}
	}
	
	public void removeKitFavorite(Player player, String name) {
			kitsFavoritos.remove(name);
			updateFunction(player, name, "remove");
	}
	
	public void updateFunction(Player player, String kit, String function) {
		if(function == "add") {
			try {
				BukkitMain.getPlugin().getMysqlBackend().getStatement().executeUpdate("INSERT INTO `kitsfavs` (`uuid`, `kits`) VALUES ('" + player.getUniqueId().toString() +"','" + kit + "')");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if(function == "remove") {
			try {
				BukkitMain.getPlugin().getMysqlBackend().getStatement().executeUpdate("DELETE FROM `kitsfavs` WHERE `uuid` ='" + player.getUniqueId().toString() + "' AND `kits` ='" + kit + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Not found function...");
		}
	}
}
