package br.com.houldmc.rankup.player.punish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.houldmc.rankup.Main;

public class PunishManager {
	
	private int id = 0;
	
	public PunishRecord punishPlayer(UUID uniqueId, Player staff, long expire, String motive, PunishType type) {
		try {
			Player punished = Bukkit.getPlayer(uniqueId);
			long start = System.currentTimeMillis();
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement insertStatment = connection.prepareStatement("INSERT INTO `punish` (`punished`, `staff`, `start`, `expire`, `motive`, `active`, `type`) VALUES (?, ?, ?, ?, ?, ?, ?);");
			
			insertStatment.setString(1, uniqueId.toString());
			insertStatment.setString(2, staff.getName());
			insertStatment.setLong(3, System.currentTimeMillis() / 1000);
			insertStatment.setLong(4, expire / 1000);
			insertStatment.setString(5, motive);
			insertStatment.setInt(6, 1);
			insertStatment.setInt(7, type.getId());
			insertStatment.execute();
			insertStatment.close();
			
			id += 1;
			PreparedStatement punishIdStatment = connection.prepareStatement("SELECT * FROM `punish` WHERE `id`=?;");
			
			punishIdStatment.setInt(1, id);
			punishIdStatment.execute();
			punishIdStatment.close();
			
			System.out.println("The player " + punished.getName() + " was banned by " + staff.getName() + " motive: " + motive);
			return new PunishRecord(id, punished, staff, start, expire, motive, true, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean unPunishPlayer(UUID uniqueId) {
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement insertStatment = connection.prepareStatement("UPDATE `punish` SET `active`=? WHERE `punished`=?;");
			insertStatment.setInt(1, 0);
			insertStatment.setString(2, uniqueId.toString());
			insertStatment.execute();
			insertStatment.close();
			
			System.out.println("The player " + uniqueId.toString() + " was unbanned.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean hasPunishActive(UUID uniqueId, PunishType... type) {
		try {
			boolean have = false;
			
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement insertStatment = connection.prepareStatement("SELECT * FROM `punish` WHERE `punished`=? AND `active`=?;");
			insertStatment.setString(1, uniqueId.toString());
			insertStatment.setInt(2, 1);
			ResultSet resultSet = insertStatment.executeQuery();
			
			while(resultSet.next()) {
				for (PunishType types : type) {
					if (resultSet.getInt(8) == types.getId())
						have = true;
				}
			}
			resultSet.close();
			insertStatment.close();
			
			return have;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<PunishRecord> getPlayerPunishRecords(UUID uniqueId) {
		Player punished = Bukkit.getPlayer(uniqueId);
		ArrayList<PunishRecord> punishList = new ArrayList<>();
		
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement insertStatment = connection.prepareStatement("SELECT * FROM `punish` WHERE `punished`=?;");
			
			insertStatment.setString(1, uniqueId.toString());
			ResultSet resultSet = insertStatment.executeQuery();
			
			while(resultSet.next()) {
				PunishRecord punish = new PunishRecord(punished);
				Player staff = Bukkit.getPlayer(resultSet.getString(3));
				punish.setStaff(staff);
				punish.setStart(resultSet.getLong(4) * 1000);

				long expire = resultSet.getLong(5);

				punish.setExpire(expire > 1 ? expire * 1000 : expire);
				punish.setMotive(resultSet.getString(6));
				punish.setActive(resultSet.getBoolean(7));
				punish.setType(PunishType.getType(resultSet.getInt(8)));
				
				punishList.add(punish);
			}
			
			resultSet.close();
			insertStatment.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return punishList;
	}
	
	public PunishRecord getPunishId(int id) {
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement insertStatment = connection.prepareStatement("SELECT * FROM `punish` WHERE `id`=?;");
			insertStatment.setInt(1, id);
			insertStatment.execute();
			insertStatment.close();
			
			return new PunishRecord(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
