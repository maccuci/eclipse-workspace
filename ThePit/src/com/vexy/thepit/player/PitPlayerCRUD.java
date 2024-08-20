package com.vexy.thepit.player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.vexy.thepit.Main;
import com.vexy.thepit.mysql.crud.TCRUD;
import com.vexy.thepit.player.scoreboard.PlayerScoreboard;

public class PitPlayerCRUD implements TCRUD<PitPlayer, UUID> {
	
	public boolean exists(UUID uuid) {
		try {
			ResultSet resultSet = Main.getPlugin().getMysql().getConnection().createStatement().executeQuery("SELECT * FROM thepit_stats WHERE `uuid` = '" + uuid + "';");
			if(resultSet.next()) {
				return resultSet.getString("uuid") != null;
			}
			resultSet.close();
			return false;
		} catch(SQLException exception) {
			exception.printStackTrace();
			return true;
		}
	}

	@Override
	public void create(PitPlayer object) {
		if(exists(object.getUuid())) {
			object.setUuid(object.getUuid());
			object.setGold(0);
			object.setLevel(1);
			object.setXp(0);
			object.setPrestige(1);
			object.setPlScoreboard(new PlayerScoreboard());
			read(object.getUuid());
			return;
		}
		try {
			Main.getPlugin().getMysql().getConnection().createStatement().execute("INSERT TO INTO thepit_stats(uuid, level, xp, prestige, gold) VALUES ('" + object.getUuid().toString() + "', '" + object.getLevel() + "', '" + object.getXp() + "', '" + object.getPrestige() + "', '" + object.getGold() + "');");
			Bukkit.getConsoleSender().sendMessage("[ThePit] PitPlayer " + object.getUuid() + " registrado com sucesso!");
		} catch (Exception e) {
			
		}
	}

	@Override
	public PitPlayer read(UUID object) {
		 try {
			 ResultSet resultSet = Main.getPlugin().getMysql().getConnection().createStatement().executeQuery("SELECT * FROM thepit_stats WHERE uuid='" + object.toString() + "';");
			 if(resultSet.next()) {
				 PitPlayer player = new PitPlayer();
				 
				 player.setUuid(object);
				 player.setId(resultSet.getInt("id"));
				 player.setLevel(resultSet.getInt("level"));
				 player.setPrestige(resultSet.getInt("prestige"));
				 player.setXp(resultSet.getInt("xp"));
				 player.setGold(resultSet.getInt("gold"));
				 Main.getPlugin().getPitPlayerManager().add(object, player);
				 Bukkit.getConsoleSender().sendMessage("[ThePit] PitPlayer " + object + " registrado com sucesso!");
				 return player;
			 } else {
				  new SQLException("Nenhum jogador encontrado.");
				  return null;
			 }
		} catch (Exception e) {}
		return null;
	}

	@Override
	public void update(PitPlayer object) {
		try {
			Main.getPlugin().getMysql().getConnection().createStatement().execute("UPDATE thepit_stats SET level='" + object.getLevel() + "', xp='" + object.getXp() + "', prestige='" + object.getPrestige() + "', gold='" + object.getGold() + "' WHERE id='" + object.getId() + "';");
			Bukkit.getConsoleSender().sendMessage("[ThePit] PitPlayer " + object.getUuid() + " atualizado com sucesso!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(UUID object) {
		try {
			Main.getPlugin().getMysql().query("DELETE FROM thepit_stats WHERE uuid='" + object.toString() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
}
