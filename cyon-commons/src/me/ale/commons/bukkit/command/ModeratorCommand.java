package me.ale.commons.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.admin.AdminManager;
import me.ale.commons.api.admin.AdminMode;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.bukkit.permission.PermissionBukkitManager;
import me.ale.commons.bukkit.permission.PermissionConfig;
import me.ale.commons.core.account.League;
import me.ale.commons.core.account.Rank;
import me.ale.commons.core.account.manager.LeagueManager;
import me.ale.commons.core.account.manager.StatsManager;
import me.ale.commons.core.command.Command;
import me.ale.commons.core.command.CommandArgs;
import me.ale.commons.core.command.CommandLoader.CommandClass;

public class ModeratorCommand implements CommandClass {
	
	@SuppressWarnings("deprecation")
	@Command(name = "gamemode", aliases = "gm", rankToUse = Rank.HELPER)
	public void gamemode(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Use: /" + cmdArgs.getLabel() + " <type> [player]");
			return;
		}
		
		if(args.length == 1) {
			GameMode gameMode = GameMode.SURVIVAL;
			try {
				gameMode = GameMode.valueOf(args[0].toUpperCase());
			} catch (Exception e) {
				try {
					gameMode = GameMode.getByValue(Integer.parseInt(args[0]));
				} catch (Exception e2) {
					player.sendMessage(CyonAPI.SERVER_PREFIX + "Este gamemode não existe.");
					return;
				}
			}
			
			if(player.getGameMode() == gameMode) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Seu gamemode já é igual a este.");
				return;
			}
			
			player.setGameMode(gameMode);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou seu gamemode para §5" + ItemBuilder.captalise(gameMode.name()));
		}
		if(args.length == 2) {
			GameMode gameMode = GameMode.SURVIVAL;
			try {
				gameMode = GameMode.valueOf(args[0].toUpperCase());
			} catch (Exception e) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Este gamemode não existe.");
				return;
			}
			
			Player target = Bukkit.getPlayer(args[1]);
			
			if(target == null) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Este jogador não está online.");
				return;
			}
			
			if(target.getGameMode() == gameMode) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "O Gamemode deste jogador já é igual a este.");
				return;
			}
			
			if(player == target) {
				player.sendMessage(CyonAPI.WARNING_PREFIX + "Você não pode alterar seu gamemode por este modo. Use apenas /gamemode <type>.");
				return;
			}
			target.setGameMode(gameMode);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o gamemode de " + target.getName() + " para §5" + ItemBuilder.captalise(gameMode.name()));
		}
	}
	
	@Command(name = "teleport", aliases = "tp", rankToUse = Rank.MOD)
	public void teleport(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Use: /teleport <x,y,z> [player]");
			return;
		}
	}
	
	@Command(name = "admin", rankToUse = Rank.HELPER)
	public void admin(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length >= 0) {
			if(new AdminManager().isAdmin(player)) {
				new AdminManager().changeMode(player, AdminMode.PLAYER);
			} else {
				new AdminManager().changeMode(player, AdminMode.ADMIN);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "league", rankToUse = Rank.OWNER)
	public void league(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		StatsManager statsManager = BukkitMain.getPlugin().getStatsManager();
		LeagueManager manager = new LeagueManager(statsManager);
		
		if(args.length <= 1) {
			player.sendMessage(CyonAPI.WARNING_PREFIX + "Use: /league <player> <league>");
			return;
		}
		
		if(args.length == 2) {
			League league = League.APPRENTICE;
			try {
				league = League.valueOf(args[1].toUpperCase());
			} catch (Exception e) {
				player.sendMessage(CyonAPI.WARNING_PREFIX + "Esta league não existe.");
				return;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!statsManager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage(CyonAPI.WARNING_PREFIX + "Este jogador nunca entrou no servidor.");
					return;
				}
				manager.setLeague(offlinePlayer.getUniqueId(), league);
			}
			if(!statsManager.exists(target.getUniqueId())) {
				player.sendMessage(CyonAPI.WARNING_PREFIX + "Este jogador nunca entrou no servidor.");
				return;
			}
			manager.setLeague(target.getUniqueId(), league);
		}
	}
	
	@Command(name = "permission", rankToUse = Rank.ADMIN)
	public void permission(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length <= 2) {
			player.sendMessage(CyonAPI.WARNING_PREFIX + "Use: /permission <add/remove> <player> <permission>");
			return;
		}
		
		if(args.length == 3) {
			if(args[0].equalsIgnoreCase("add")) {
				String permissionName = args[2];
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					return;
				}
				PermissionConfig config = new PermissionConfig();
				config.addPermissionPlayer(target.getName(), permissionName);
				PermissionBukkitManager manager = new PermissionBukkitManager();
				manager.addPermissionName(target, permissionName);
				manager.refreshPerms(target);
				player.sendMessage(CyonAPI.WARNING_PREFIX + "§aVocê sedeu a permissão " + permissionName + " para o jogador " + target.getName() + ".");
			}
			if(args[0].equalsIgnoreCase("remove")) {
				String permissionName = args[2];
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					return;
				}
				PermissionConfig config = new PermissionConfig();
				config.addPermissionPlayer(target.getName(), permissionName);
				PermissionBukkitManager manager = new PermissionBukkitManager();
				manager.removePermission(target, permissionName);
				manager.refreshPerms(target);
				player.sendMessage(CyonAPI.WARNING_PREFIX + "Você removeu a permissão " + permissionName + " do jogador " + target.getName() + ".");
			}
		}
	}
}
