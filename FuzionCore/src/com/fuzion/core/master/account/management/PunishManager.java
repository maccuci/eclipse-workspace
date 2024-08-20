package com.fuzion.core.master.account.management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.bukkit.BukkitMain;

import net.md_5.bungee.api.ChatColor;

public class PunishManager {
	
	public static String defaultReason = "Motivo indefinido.";
	
	public static enum Types {
		BAN, MUTE, KICK;
	}
	
	public static enum Durations {
		PERMANENT, TEMPORARY;
	}
	
	private Types type;
	private Durations duration;
	private Player player;
	private OfflinePlayer offlinePlayer;
	private String reason;
	private long expire;
	
	public PunishManager(Types type, Durations duration, Player player, String reason, long expire) {
		this.type = type;
		this.duration = duration;
		this.player = player;
		this.offlinePlayer = null;
		this.reason = reason;
		this.expire = expire;
	}
	
	public PunishManager(Types type, Durations duration, OfflinePlayer offlinePlayer, String reason, long expire) {
		this.type = type;
		this.duration = duration;
		this.player = null;
		this.offlinePlayer = offlinePlayer;
		this.reason = reason;
		this.expire = expire;
	}
	
	private boolean isOnline() {
		return this.player != null && this.offlinePlayer == null;
	}
	
	public void punish(Player staffer) {
		if(this.type.equals(Types.BAN)) {
			try {
				BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("INSERT INTO `punish` (`uuid`, `nickname`, `type`, `duration`, `reason`, `staffer`, `date`, `expire`) VALUES ('" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "', '" + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "', '" + Types.BAN + "', '" + Durations.valueOf(this.duration.equals(Durations.PERMANENT) ? Durations.PERMANENT.name() : Durations.TEMPORARY.name()) + "', '" + this.reason + "', '" + staffer.getName() + "', '" + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()) + "', '" + this.expire + "')");
				staffer.sendMessage(ChatColor.GREEN + "Você baniu " + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "(" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + ") " + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " por " + this.reason + "!");
				if(isOnline()) {
					this.player.kickPlayer(ChatColor.RED + "Sua conta foi suspensa " + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + "!\n\nMotivo: " + this.reason + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "" : "\nDuração: " + ItemBuilder.getMessage(this.expire)) + "\n\nAdquira seu unban em:\nloja.fuzion-network.com");
				}
			} catch(SQLException exception) {
				exception.printStackTrace();
				staffer.sendMessage(ChatColor.RED + "Não foi possível banir o jogador. Tente novamente mais tarde.");
			}
			return;
		}
		if(this.type.equals(Types.MUTE)) {
			try {
				BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("INSERT INTO `punish` (`uuid`, `nickname`, `type`, `duration`, `reason`, `staffer`, `date`, `expire`) VALUES ('" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "', '" + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "', '" + Types.MUTE + "', '" + Durations.valueOf(this.duration.equals(Durations.PERMANENT) ? Durations.PERMANENT.name() : Durations.TEMPORARY.name()) + "', '" + this.reason + "', '" + staffer.getName() + "', '" + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()) + "', '" + this.expire + "')");
				staffer.sendMessage(ChatColor.GREEN + "Você mutou " + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "(" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + ") " + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " por " + this.reason + "!");
				if(isOnline()) {
					this.player.sendMessage(ChatColor.RED + "Você foi mutado " + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " por " + this.reason + "!" + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "" : "\nExpira em: " + ItemBuilder.getMessage(this.expire)));
				}
			} catch(SQLException exception) {
				exception.printStackTrace();
				staffer.sendMessage(ChatColor.RED + "Não foi possível banir o jogador. Tente novamente mais tarde.");
			}
			return;
		}
		if(this.type.equals(Types.KICK)) {
			try {
				BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("INSERT INTO `punish` (`uuid`, `nickname`, `type`, `duration`, `reason`, `staffer`, `date`, `expire`) VALUES ('" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "', '" + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "', '" + Types.KICK + "', '" + Durations.valueOf(this.duration.equals(Durations.PERMANENT) ? Durations.PERMANENT.name() : Durations.TEMPORARY.name()) + "', '" + this.reason + "', '" + staffer.getName() + "', '" + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()) + "', '" + this.expire + "')");
				staffer.sendMessage(ChatColor.GREEN + "Você kickou " + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "(" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + ") por " + this.reason + "!");
				if(isOnline()) {
					this.player.kickPlayer(ChatColor.RED + "Você foi expulso do servidor!\n\nMotivo: " + this.reason + "\n\nPedimos desculpas pelo ocorrido.");
				}
			} catch(SQLException exception) {
				exception.printStackTrace();
				staffer.sendMessage(ChatColor.RED + "Não foi possível kickar o jogador. Tente novamente mais tarde.");
			}
			return;
		}
	}
	
	public void punish(CommandSender staffer) {
		if(this.type.equals(Types.BAN)) {
			try {
				BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("INSERT INTO `punish` (`uuid`, `nickname`, `type`, `duration`, `reason`, `staffer`, `date`, `expire`) VALUES ('" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "', '" + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "', '" + Types.BAN + "', '" + Durations.valueOf(this.duration.equals(Durations.PERMANENT) ? Durations.PERMANENT.name() : Durations.TEMPORARY.name()) + "', '" + this.reason + "', '" + staffer.getName() + "', '" + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()) + "', '" + this.expire + "')");
				staffer.sendMessage(ChatColor.GREEN + "Você baniu " + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "(" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + ") " + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " por " + this.reason + "!");
				if(isOnline()) {
					this.player.kickPlayer(ChatColor.RED + "Sua conta foi suspensa " + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + "!\n\nMotivo: " + this.reason + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "" : "\nDuração: " + ItemBuilder.getMessage(this.expire)) + "\n\nAdquira seu unban em:\nloja.fuzion-network.com");
				}
			} catch(SQLException exception) {
				exception.printStackTrace();
				staffer.sendMessage(ChatColor.RED + "Não foi possível banir o jogador. Tente novamente mais tarde.");
			}
			return;
		}
		if(this.type.equals(Types.MUTE)) {
			try {
				BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("INSERT INTO `punish` (`uuid`, `nickname`, `type`, `duration`, `reason`, `staffer`, `date`, `expire`) VALUES ('" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "', '" + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "', '" + Types.MUTE + "', '" + Durations.valueOf(this.duration.equals(Durations.PERMANENT) ? Durations.PERMANENT.name() : Durations.TEMPORARY.name()) + "', '" + this.reason + "', '" + staffer.getName() + "', '" + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()) + "', '" + this.expire + "')");
				staffer.sendMessage(ChatColor.GREEN + "Você mutou " + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "(" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + ") " + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " por " + this.reason + "!");
				if(isOnline()) {
					this.player.sendMessage(ChatColor.RED + "Você foi mutado " + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " por " + this.reason + "!" + String.valueOf(this.duration.equals(Durations.PERMANENT) ? "" : "\nExpira em: " + ItemBuilder.getMessage(this.expire)));
				}
			} catch(SQLException exception) {
				exception.printStackTrace();
				staffer.sendMessage(ChatColor.RED + "Não foi possível banir o jogador. Tente novamente mais tarde.");
			}
		}
		if(this.type.equals(Types.KICK)) {
			try {
				BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("INSERT INTO `punish` (`uuid`, `nickname`, `type`, `duration`, `reason`, `staffer`, `date`, `expire`) VALUES ('" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "', '" + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "', '" + Types.KICK + "', '" + Durations.valueOf(this.duration.equals(Durations.PERMANENT) ? Durations.PERMANENT.name() : Durations.TEMPORARY.name()) + "', '" + this.reason + "', '" + staffer.getName() + "', '" + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()) + "', '" + this.expire + "')");
				staffer.sendMessage(ChatColor.GREEN + "Você kickou " + String.valueOf(isOnline() ? this.player.getName() : this.offlinePlayer.getName()) + "(" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + ") por " + this.reason + "!");
				if(isOnline()) {
					this.player.kickPlayer(ChatColor.RED + "Você foi expulso do servidor!\n\nMotivo: " + this.reason + "\n\nPedimos desculpas pelo ocorrido.");
				}
			} catch(SQLException exception) {
				exception.printStackTrace();
				staffer.sendMessage(ChatColor.RED + "Não foi possível kickar o jogador. Tente novamente mais tarde.");
			}
			return;
		}
	}
	
	public boolean isPunished() {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().resultSet("SELECT * FROM `punish` WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "';");
			if(resultSet.next()) {
				return resultSet.getString("uuid") != null;
			}
			resultSet.close();
			return false;
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(isOnline()) {
				this.player.kickPlayer("§cAlgo deu errado durante a punição.");
			}
			return true;
		}
	}
	
	public Types getType() {
		if(!isPunished()) {
			return Types.BAN;
		}
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().resultSet("SELECT * FROM `punish` WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "';");
			if(resultSet.next()) {
				return Types.valueOf(resultSet.getString("type"));
			}
			resultSet.close();
			return null;
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(isOnline()) {
				this.player.kickPlayer("§cAlgo deu errado durante a punição.");
			}
			return Types.BAN;
		}
	}
	
	public Durations getDuration() {
		if(!isPunished()) {
			return Durations.PERMANENT;
		}
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().resultSet("SELECT * FROM `punish` WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "';");
			if(resultSet.next()) {
				return Durations.valueOf(resultSet.getString("duration"));
			}
			resultSet.close();
			return null;
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(isOnline()) {
				this.player.kickPlayer("§cAlgo deu errado durante a punição.");
			}
			return Durations.PERMANENT;
		}
	}
	
	public long getExpire() {
		if(!isPunished()) {
			return 0L;
		}
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().resultSet("SELECT * FROM `punish` WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "';");
			if(resultSet.next()) {
				return resultSet.getLong("expire");
			}
			resultSet.close();
			return 0L;
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(isOnline()) {
				this.player.kickPlayer("§cAlgo deu errado durante a punição.");
			}
			return 0L;
		}
	}
	
	public String get(String column) {
		if(!isPunished()) {
			return null;
		}
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().resultSet("SELECT * FROM `punish` WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "';");
			if(resultSet.next()) {
				return resultSet.getString(column);
			}
			resultSet.close();
			return null;
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(isOnline()) {
				this.player.kickPlayer("§cAlgo deu errado durante a punição.");
			}
			return null;
		}
	}
	
	public void set(String column, String value) {
		if(!isPunished()) {
			return;
		}
		try {
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `punish` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(isOnline()) {
				this.player.kickPlayer("§cAlgo deu errado durante a punição.");
			}
		}
	}
	
	public void set(String column, long value) {
		if(!isPunished()) {
			return;
		}
		try {
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `punish` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(isOnline()) {
				this.player.kickPlayer("§cAlgo deu errado durante a punição.");
			}
		}
	}
	
	public void unPunish() {
		BukkitMain.getPlugin().getMysqlBackend().execute("DELETE FROM `punish` WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "';");
	}
	
	public void unBan() {
		BukkitMain.getPlugin().getMysqlBackend().execute("DELETE FROM `punish` WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "' AND `type` = '" + Types.BAN + "';");
	}
	
	public void unMute() {
		BukkitMain.getPlugin().getMysqlBackend().execute("DELETE FROM `punish` WHERE `uuid` = '" + String.valueOf(isOnline() ? this.player.getUniqueId().toString() : this.offlinePlayer.getUniqueId().toString()) + "' AND `type` = '" + Types.MUTE + "';");
	}
}