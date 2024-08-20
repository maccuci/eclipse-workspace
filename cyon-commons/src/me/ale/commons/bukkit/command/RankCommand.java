package me.ale.commons.bukkit.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.Rank;
import me.ale.commons.core.account.manager.RankManager;
import me.ale.commons.core.account.manager.TagManager;
import me.ale.commons.core.command.Command;
import me.ale.commons.core.command.CommandArgs;
import me.ale.commons.core.command.Completer;
import me.ale.commons.core.command.CommandLoader.CommandClass;

public class RankCommand implements CommandClass {
	
	
	@SuppressWarnings("deprecation")
	@Command(name = "setrank", aliases = {"rankset", "rank"}, rankToUse = Rank.ADMIN)
	public void rank(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		RankManager manager = BukkitMain.getPlugin().getRankManager();
		
		if(args.length <= 1) {
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Use /" + cmdArgs.getLabel() + " <rank> [time]");
			return;
		}
		if(args.length == 2) {
			if(!manager.hasGroupPermission(player.getUniqueId(), Rank.ADMIN)) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não tem permissão para usar esse comando.");
				return;
			}
			Rank rank = Rank.NORMAL;
			try {
				rank = Rank.valueOf(args[1].toUpperCase());
			} catch (Exception e) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Este rank não existe.");
				return;
			}
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!manager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage(CyonAPI.SERVER_PREFIX + "Este jogador nunca entrou no servidor.");
					return;
				}
				
				if(!manager.hasGroupPermission(player.getUniqueId(), manager.getRank(offlinePlayer.getUniqueId()))) {
					player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não pode alterar o rank deste jogador.");
					return;
				}
				
				if(manager.hasGroupPermission(player.getUniqueId(), rank)) {
					player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não pode alterar ranks acima do seu.");
					return;
				}
				
				if(manager.hasGroup(offlinePlayer.getUniqueId(), rank)) {
					player.sendMessage(CyonAPI.SERVER_PREFIX + "Este jogador já possui este rank.");
					return;
				}
				manager.set(offlinePlayer.getUniqueId(), "playerrank", rank.name());
				manager.set(offlinePlayer.getUniqueId(), "temporary", 0);
				manager.set(offlinePlayer.getUniqueId(), "expire", 0L);
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o rank do jogador " + offlinePlayer.getName() + " para " + rank.getColor() + ItemBuilder.captalise(rank.name()));
				return;
			}
			if(!manager.hasGroupPermission(player.getUniqueId(), manager.getRank(target.getUniqueId()))) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não pode alterar o rank deste jogador.");
				return;
			}
			
			if(!manager.hasGroupPermission(player.getUniqueId(), rank)) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não pode alterar ranks acima do seu.");
				return;
			}
			
			if(manager.hasGroup(target.getUniqueId(), rank)) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Este jogador já possui este rank.");
				return;
			}
			manager.set(target.getUniqueId(), "playerrank", rank.name());
			manager.set(target.getUniqueId(), "temporary", 0);
			manager.set(target.getUniqueId(), "expire", 0L);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o rank do jogador " + target.getName() + " para " + rank.getColor() + ItemBuilder.captalise(rank.name()));
			new TagManager(target).updateAll();
		}
		if(args.length == 3) {
			Rank rank = Rank.NORMAL;
			try {
				rank = Rank.valueOf(args[1].toUpperCase());
			} catch (Exception e) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Este rank não existe.");
				return;
			}
			
			if(rank.equals(Rank.NORMAL)) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Este rank não pode ser setado temporariamente.");
				return;
			}
			String timeString = args[2];
			long time = 0L;
			try {
				time = ItemBuilder.fromString(timeString);
			} catch(Exception exception) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Formato de tempo incorreto. Utilize: 1d,1h,1m,1s.");
				time = 0L;
				return;
			}
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!manager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage(CyonAPI.SERVER_PREFIX + "Este jogador nunca entrou no servidor.");
					return;
				}
				
				if(!manager.hasGroupPermission(player.getUniqueId(), manager.getRank(offlinePlayer.getUniqueId()))) {
					player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não pode alterar o rank deste jogador.");
					return;
				}
				
				if(manager.hasGroupPermission(player.getUniqueId(), rank)) {
					player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não pode alterar ranks acima do seu.");
					return;
				}
				
				if(manager.hasGroup(offlinePlayer.getUniqueId(), rank)) {
					player.sendMessage(CyonAPI.SERVER_PREFIX + "Este jogador já possui este rank.");
					return;
				}
				manager.set(offlinePlayer.getUniqueId(), "playerrank", rank.name());
				manager.set(offlinePlayer.getUniqueId(), "temporary", 1);
				manager.set(offlinePlayer.getUniqueId(), "expire", time);
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o rank do jogador " + offlinePlayer.getName() + " para " + rank.getColor() + ItemBuilder.captalise(rank.name()) + " §7durante " + ItemBuilder.getMessage(time));
				return;
			}
			if(!manager.exists(target.getUniqueId())) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Este jogador nunca entrou no servidor.");
				return;
			}
			
			if(!manager.hasGroupPermission(player.getUniqueId(), manager.getRank(target.getUniqueId()))) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não pode alterar o rank deste jogador.");
				return;
			}
			
			if(manager.hasGroupPermission(player.getUniqueId(), rank)) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não pode alterar ranks acima do seu.");
				return;
			}
			
			if(manager.hasGroup(target.getUniqueId(), rank)) {
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Este jogador já possui este rank.");
				return;
			}
			manager.set(target.getUniqueId(), "playerrank", rank.name());
			manager.set(target.getUniqueId(), "temporary", 1);
			manager.set(target.getUniqueId(), "expire", time);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você alterou o rank do jogador " + target.getName() + " para " + rank.getColor() + ItemBuilder.captalise(rank.name()) + " §7durante " + ItemBuilder.getMessage(time));
			new TagManager(target).updateAll();
		}
	}
	
	@Completer(name = "setrank", aliases = {"rankset", "rank"})
	public List<String> rankcompleter(CommandArgs cmdArgs) {
		RankManager manager = BukkitMain.getPlugin().getRankManager();
		List<String> matches = new ArrayList<>();
		String[] args = cmdArgs.getArgs();
		
		if(args.length <= 1 || args.length >= 3) {
			return null;
		}
		if(args.length == 2) {
			String search = args[1].toLowerCase();
			for(Rank groups : Rank.values()) {
				if(manager.getRank(cmdArgs.getPlayer().getUniqueId()).equals(Rank.ADMIN)) {
					if(groups.ordinal() <= Rank.SIMPLE.ordinal()) {
						if(groups.name().toLowerCase().startsWith(search)) {
							matches.add(ItemBuilder.captalise(groups.name()));
						}
					}
				} else {
					if(groups.name().toLowerCase().startsWith(search)) {
						matches.add(ItemBuilder.captalise(groups.name()));
					}
				}
			}
		}
		return matches;
	}
}
