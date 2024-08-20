package br.com.houldmc.rankup.player.account.crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.manager.rank.list.RankList;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;
import br.com.houldmc.rankup.player.group.list.GroupList;
import br.com.houldmc.rankup.player.punish.PunishManager;
import br.com.houldmc.rankup.player.punish.PunishRecord;
import br.com.houldmc.rankup.player.punish.PunishType;
import lombok.Getter;

public class RankUPPlayerCrud implements ICRUD<RankUPPlayer, UUID>{

	private static final Executor saveAsyncExecutor = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setNameFormat("Save Async Thread").build());
	@Getter
	public static final HashMap<Integer, PunishRecord> punishRecords = new HashMap<>();
	
	private PunishManager punishManager = new PunishManager();
	
	@Override
	public void create(RankUPPlayer object) {
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uniqueId=?");
			
			preparedStatement.setString(1, object.getUniqueId().toString());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				object.setGroup(GroupList.valueOf(resultSet.getString(3)));
				object.setGroupTemporary(resultSet.getBoolean(4));
				object.setGroupExpire(resultSet.getLong(5));
				object.setRank(RankList.valueOf(resultSet.getString(6)));
				object.setMoney(resultSet.getInt(7));
				object.setCash(resultSet.getInt(8));
				object.setSouls(resultSet.getInt(9));
				
				for (PunishRecord punish : punishManager.getPlayerPunishRecords(object.getUniqueId())) {
					punishRecords.put(punish.getId(), punish);
				}
				
				resultSet.close();
			} else {
				object.setMoney(0);
				object.setCash(0);
				object.setSouls(0);
				
				PreparedStatement accountInsert = connection.prepareStatement("INSERT INTO `players` (`uniqueId`, `nick`, `group`, `group_temporary`, `group_expire`, `rank`, `money`, `cash`, `souls`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
				accountInsert.setString(1, object.getUniqueId().toString());
				accountInsert.setString(2, "");
				accountInsert.setString(3, GroupList.NORMAL.getName().toUpperCase());
				accountInsert.setBoolean(4, false);
				accountInsert.setLong(5, -1);
				accountInsert.setString(6, RankList.FARMER_III.name());
				accountInsert.setInt(7, 0);
				accountInsert.setInt(8, 0);
				accountInsert.setInt(9, 0);
				accountInsert.execute();
				accountInsert.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Erro aqui caralho.");
		}
	}

	@Override
	public RankUPPlayer read(UUID object) {
		try {
			RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(object);
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uniqueId=?");
			
			preparedStatement.setString(1, object.toString());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				rankUPPlayer.setGroup(GroupList.valueOf(resultSet.getString(3)));
				rankUPPlayer.setGroupTemporary(resultSet.getBoolean(4));
				rankUPPlayer.setGroupExpire(resultSet.getLong(5));
				rankUPPlayer.setRank(RankList.valueOf(resultSet.getString(6)));
				rankUPPlayer.setMoney(resultSet.getInt(7));
				rankUPPlayer.setCash(resultSet.getInt(8));
				rankUPPlayer.setSouls(resultSet.getInt(9));
				
				for (PunishRecord punish : punishManager.getPlayerPunishRecords(object)) {
					punishRecords.put(punish.getId(), punish);
				}
				
				return rankUPPlayer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void update(RankUPPlayer object) {
			saveAsyncExecutor.execute(() -> {
				try {
					PreparedStatement statement = Main.getPlugin().getManager().getMySQL().getSlaveConnection().prepareStatement("UPDATE `players` SET `group`=?, `group_temporary`=?, `group_expire`=?, `rank`=?, `money`=?, `cash`=?, `souls`=?");
					
					
					statement.setString(1, object.getGroup().name());
					statement.setBoolean(2, object.isGroupTemporary());
					statement.setLong(3, object.getGroupExpire());
					statement.setString(4, object.getRank().name());
					statement.setDouble(5, object.getMoney());
					statement.setInt(6, object.getCash());
					statement.setInt(7, object.getSouls());
					
					statement.execute();
					
					for (PunishRecord punish : punishManager.getPlayerPunishRecords(object.getUniqueId())) {
						punishRecords.put(punish.getId(), punish);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
	}

	public static PunishRecord getLastActiveBan() {
		for (PunishRecord punish : getPunishRecords().values()) {
			if (punish.getType().equals(PunishType.BAN) || punish.getType().equals(PunishType.TEMPBAN))
				if (punish.isActive())
					return punish;
		}
		return null;
	}

	@Override
	public void delete(UUID object) {
		
	}

}
