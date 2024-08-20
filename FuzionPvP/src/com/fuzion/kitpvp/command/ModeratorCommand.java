package com.fuzion.kitpvp.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.PunishManager;
import com.fuzion.core.master.account.management.PunishManager.Durations;
import com.fuzion.core.master.account.management.PunishManager.Types;
import com.fuzion.core.master.account.management.StatsManager;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;
import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.event.PlayerWarpJoinEvent;
import com.fuzion.kitpvp.manager.kit.KitManager;
import com.fuzion.kitpvp.manager.kit.TeleportConfig;
import com.fuzion.kitpvp.manager.onevsone.Warp1v1;
import com.fuzion.kitpvp.manager.warp.WarpManager;
import com.fuzion.kitpvp.manager.warp.Warps;

public class ModeratorCommand implements CommandClass {
	
	@Command(name = "warp")
	public void warp(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length != 1) {
			player.sendMessage("§cUse: /warp <warpName>");
			return;
		}
		
		if(args.length == 1) {
			Warps warp = Warps.NONE;
			try {
				warp = Warps.valueOf(args[0].toUpperCase());
			} catch (Exception e) {
				player.sendMessage("§cEsta warp não existe.");
				return;
			}
			
			if(new KitManager().getKitName(player) != "Nenhum") {
				player.sendMessage("§cVocê está com um kit.");
				return;
			}
			
			WarpManager.teleport(player, warp);
			player.sendMessage("§aVocê foi teleportado para warp " + warp.getName());
			player.teleport(WarpManager.getLocation(args[0]));
	}
}
	
	@Command(name = "setwarp")
	public void setwarp(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		WarpManager warpManager = Main.getPlugin().getWarpManager();
		
		if(args.length != 1) {
			player.sendMessage("§cUse: /setwarp <warpName>");
			return;
		}
		
		if(args.length == 1) {
			if(args[0].contains("fps") || args[0].contains("challenge")) {
			
			player.sendMessage("§aVocê setou a warp " + ItemBuilder.captalise(args[0]) + " na sua localização.");
			warpManager.setWarp(args[0], player.getLocation());
			}
		}
	}
	
	@Command(name = "1v1")
	public void warp1v1(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length >= 0) {
			if(WarpManager.getLocation("1v1Spawn") == null) {
				player.sendMessage("§cWarp não definida.");
				return;
			}
			PlayerWarpJoinEvent event = new PlayerWarpJoinEvent(player, "1v1");
			Bukkit.getPluginManager().callEvent(event);
		}
	}
	
	@Command(name = "set1v1", groupToUse = Group.MOD)
	public void set1v1(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /set1v1[spawn/pos1/pos2]>");
			return;
		}
		if(args.length == 1) {
			if(args[0].equals("spawn")) {
				new WarpManager().setWarp("1v1Spawn", player.getLocation());
				player.sendMessage("§aVocê setou o spawn da 1v1.");
			}
			if(args[0].equals("pos1")) {
				new WarpManager().setWarp("1v1pos1", player.getLocation());
				player.sendMessage("§aVocê setou a primeira posição da 1v1.");
			}
			if(args[0].equals("pos2")) {
				new WarpManager().setWarp("1v1pos2", player.getLocation());
				player.sendMessage("§aVocê setou a segunda posição da 1v1.");
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "account", aliases = "acc")
	public void account(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		StatsManager statsManager = new StatsManager();
		GroupManager groupManager = new GroupManager();
		Group group = new GroupManager().getGroup(player.getUniqueId());
		
		if (args.length == 0) {
			player.sendMessage("§eInformações sua abaixo:");
			player.sendMessage(" ");
			player.sendMessage("§eKills: " + statsManager.get(player.getUniqueId(), "kills"));
			player.sendMessage("§eDeaths: " + statsManager.get(player.getUniqueId(), "deaths"));
			player.sendMessage("§eStreak: " + statsManager.get(player.getUniqueId(), "streak"));
			player.sendMessage("§eXP: " + statsManager.get(player.getUniqueId(), "xp"));
			player.sendMessage(" ");
			player.sendMessage("§eGrupo: " + group.getColor() + group.name() + (groupManager.isTemporary(player.getUniqueId()) ? " §eexpira em " + ItemBuilder.getMessage(groupManager.getExpire(player.getUniqueId())) : " §epermanente"));
			player.sendMessage("§ePosição: " + Main.getPlugin().getPositionManager().getPosition(player) + "°");
			player.sendMessage(" ");
			PunishManager punish = new PunishManager(null, null, player, null, 0);
			if(punish.isPunished()) {
				if(punish.getType().equals(Types.MUTE)) {
					player.sendMessage("§eMutado " + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " em " + punish.get("date").split(" ")[0] + " por " + punish.get("reason") + "!" + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "" : " §7(" + ItemBuilder.getMessage(punish.getExpire()) + ")"));
				}
			}
		} else if (args.length == 1) {
			Player target = Bukkit.getPlayerExact(args[0]);
			if (target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!statsManager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				group = new GroupManager().getGroup(offlinePlayer.getUniqueId());
				player.sendMessage("§eInformações do jogador " + offlinePlayer.getName() + " abaixo:");
				player.sendMessage(" ");
				player.sendMessage("§eKills: " + statsManager.get(offlinePlayer.getUniqueId(), "kills"));
				player.sendMessage("§eDeaths: " + statsManager.get(offlinePlayer.getUniqueId(), "deaths"));
				player.sendMessage("§eStreak: " + statsManager.get(offlinePlayer.getUniqueId(), "streak"));
				player.sendMessage("§eXP: " + statsManager.get(offlinePlayer.getUniqueId(), "xp"));
				player.sendMessage(" ");
				player.sendMessage("§eGrupo: " + group.getColor() + group.name() + (groupManager.isTemporary(offlinePlayer.getUniqueId()) ? " §eexpira em " + ItemBuilder.getMessage(groupManager.getExpire(offlinePlayer.getUniqueId())) : " §epermanente"));
				player.sendMessage("§ePosição: " + Main.getPlugin().getPositionManager().getPosition(offlinePlayer) + "°");
				player.sendMessage(" ");
				PunishManager punish = new PunishManager(null, null, offlinePlayer, null, 0);
				if(punish.isPunished()) {
					if(punish.getType().equals(Types.BAN)) {
						player.sendMessage("§eBanido " + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " em " + punish.get("date").split(" ")[0] + " por " + punish.get("reason") + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "" : " (" + ItemBuilder.getMessage(punish.getExpire()) + ")"));
					} else if(punish.getType().equals(Types.MUTE)) {
						player.sendMessage("§eMutado " + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " em " + punish.get("date").split(" ")[0] + " por " + punish.get("reason") + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "" : " (" + ItemBuilder.getMessage(punish.getExpire()) + ")"));
					}
				}
				return;
			}
			group = new GroupManager().getGroup(target.getUniqueId());
			player.sendMessage("§eInformações do jogador " + target.getName() + " abaixo:");
			player.sendMessage(" ");
			player.sendMessage("§eKills: " + statsManager.get(target.getUniqueId(), "kills"));
			player.sendMessage("§eDeaths: " + statsManager.get(target.getUniqueId(), "deaths"));
			player.sendMessage("§eStreak: " + statsManager.get(target.getUniqueId(), "streak"));
			player.sendMessage("§eXP: " + statsManager.get(target.getUniqueId(), "xp"));
			player.sendMessage(" ");
			player.sendMessage("§ePosição: " + Main.getPlugin().getPositionManager().getPosition(target) + "°");
			player.sendMessage("§eGrupo: " + group.getColor() + group.name() + (groupManager.isTemporary(target.getUniqueId()) ? " §eexpira em " + ItemBuilder.getMessage(groupManager.getExpire(target.getUniqueId())) : " §epermanente"));
			player.sendMessage(" ");
			PunishManager punish = new PunishManager(null, null, target, null, 0);
			if(punish.isPunished()) {
				if(punish.getType().equals(Types.BAN)) {
					player.sendMessage("§eBanido " + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " em " + punish.get("date").split(" ")[0] + " por " + punish.get("reason") + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "" : " (" + ItemBuilder.getMessage(punish.getExpire()) + ")"));
				} else if(punish.getType().equals(Types.MUTE)) {
					player.sendMessage("§eMutado " + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "permanentemente" : "temporariamente") + " em " + punish.get("date").split(" ")[0] + " por " + punish.get("reason") + String.valueOf(punish.getDuration().equals(Durations.PERMANENT) ? "" : " (" + ItemBuilder.getMessage(punish.getExpire()) + ")"));
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "espectar", groupToUse = Group.TRIAL)
	public void spect(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /espectar <playerName>");
			return;
		}
		if(args.length == 1) {
			Player target = Bukkit.getPlayerExact(args[0]);
			
			if(target == null) {
				player.sendMessage("§cEste jogador não esta online.");
				return;
			}
			
			if(!Warp1v1.playersIn1v1.contains(target)) {
				player.sendMessage("§cEste jogador não está na 1v1.");
				return;
			}
			if(!Warp1v1.fighting.containsKey(target)) {
				player.sendMessage("§cEste jogador não está em uma batalha ainda.");
				return;
			}

			for (Player players : Bukkit.getOnlinePlayers()) {
				player.hidePlayer(players);
				player.showPlayer(target);
				player.showPlayer(Warp1v1.fighting.get(target).getPlayer());
				player.teleport(target);
			}
		}
	}
	
	@Command(name = "settp", aliases = "tpset", groupToUse = Group.BUILDER)
	public void settp(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length >= 0) {
			TeleportConfig config = new TeleportConfig();
			config.setup();
			config.setLoc(player.getLocation());
			player.sendMessage("§eVocê setou a localização §b" + config.pos());
		}
	}
}
