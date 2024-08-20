package com.fuzion.core.bukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.api.admin.AdminAPI.AdminMode;
import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.bukkit.manager.ReportManager;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.PunishManager;
import com.fuzion.core.master.account.management.StatsManager;
import com.fuzion.core.master.account.management.PunishManager.Durations;
import com.fuzion.core.master.account.management.PunishManager.Types;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;
import com.fuzion.core.master.data.language.Language;
import com.fuzion.core.master.data.language.LanguageManager;

public class ModeratorCommand implements CommandClass {
	
	public static List<Player> staffChat = new ArrayList<>();
	public static List<Player> freeze = new ArrayList<>();
	
	public static enum TypeStats {
		KILLS, CRATES, DEATHS, XP, STREAK;
	}
	
	@Command(name = "admin", groupToUse = Group.TRIAL)
	public void admin(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			if(!AdminAPI.admin.contains(player.getUniqueId())) {
				new AdminAPI().updateMode(player, AdminMode.ADMIN);
			} else if(AdminAPI.admin.contains(player.getUniqueId())) {
				new AdminAPI().updateMode(player, AdminMode.PLAYER);
			}
		}
	}
	
	@Command(name = "report")
	public void report(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /report <playerName> [reason]");
			return;
		}
		
		if(args.length >= 2) {
			Player target = Bukkit.getPlayerExact(args[0]);
			if(target == null) {
				player.sendMessage("§cAlvo não encontrado.");
				return;
			}
			
			if(CooldownAPI.isInCooldown(player.getUniqueId(), "report")) {
				player.sendMessage("§cVocê ainda não pode reportar. Espere durante " + DateUtils.formatDifference(CooldownAPI.getTimeLeft(player.getUniqueId(), "report")));
				return;
			}
			
			String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
			
			player.sendMessage("§aVocê reportou o jogador " + target.getName() + " pelo motivo " + reason);
			ReportManager.addReport(player, target, reason);
			CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), "report", 10);
			cooldown.start();
		}
	}
	
	@Command(name = "reports", groupToUse = Group.TRIAL)
	public void reports(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			ReportManager.menu(player, 1);
			return;
		}
	}
	
	@Command(name = "staffchat", aliases = "sc", groupToUse = Group.YOUTUBERPLUS)
	public void staffchat(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			if(!staffChat.contains(player)) {
				staffChat.add(player);
				player.sendMessage("§aVocê entrou no staffchat!");
			} else {
				staffChat.remove(player);
				player.sendMessage("§cVocê saiu do staffchat!");
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "givestats", groupToUse = Group.DEVELOPER)
	public void give(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		StatsManager statsManager = new StatsManager();
		
		 if(args.length != 3) {
			 player.sendMessage("§cUse: /givestats <playerName> <type> <amount>");
			 return;
		 }
		 if(args.length == 3) {
			 Player target = Bukkit.getPlayerExact(args[0]);
			 TypeStats type = TypeStats.KILLS;
			try {
				type = TypeStats.valueOf(args[1].toUpperCase());
			} catch (Exception e) {
				player.sendMessage("§cEste tipo não existe.");
				return;
			}
			Integer amount = 0;
			try {
				amount = Integer.valueOf(args[2]);
			} catch (Exception e) {
				player.sendMessage("§cDigite apenas números.");
			}
				
			if (target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if (!statsManager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				statsManager.set(offlinePlayer.getUniqueId(), type.name(), statsManager.get(offlinePlayer.getUniqueId(), type.name()) + amount);
				player.sendMessage("§aVocê adicionou no status de " + offlinePlayer.getName() + " do tipo " + type.name() + " para o valor " + amount + ".");
				return;
			}
			statsManager.set(target.getUniqueId(), type.name(), statsManager.get(target.getUniqueId(), type.name()) + amount);
			player.sendMessage("§aVocê adicionou no status de " + target.getName() + " do tipo " + type.name() + " para o valor " + amount + ".");
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "gamemode", aliases = "gm", groupToUse = Group.TRIAL)
	public void gamemode(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /" + cmdArgs.getLabel() + " <type> [player]");
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
					player.sendMessage("§cEste gamemode não existe.");
					return;
				}
			}
			
			if(player.getGameMode() == gameMode) {
				player.sendMessage("§cSeu gamemode já é igual a este.");
				return;
			}
			
			player.setGameMode(gameMode);
			player.sendMessage("§cVocê alterou seu gamemode para §5" + ItemBuilder.captalise(gameMode.name()));
		}
		if(args.length == 2) {
			GameMode gameMode = GameMode.SURVIVAL;
			try {
				gameMode = GameMode.valueOf(args[0].toUpperCase());
			} catch (Exception e) {
				try {
					gameMode = GameMode.getByValue(Integer.parseInt(args[0]));
				} catch (Exception e2) {
					player.sendMessage("§cEste gamemode não existe.");
					return;
				}
			}
			
			Player target = Bukkit.getPlayer(args[1]);
			
			if(target == null) {
				player.sendMessage("§cEste jogador não está online.");
				return;
			}
			
			if(target.getGameMode() == gameMode) {
				player.sendMessage("§cO Gamemode deste jogador já é igual a este.");
				return;
			}
			
			if(player == target) {
				player.sendMessage("§cVocê não pode alterar seu gamemode por este modo. Use apenas /gamemode <type>.");
				return;
			}
			target.setGameMode(gameMode);
			player.sendMessage("§eVocê alterou o gamemode de " + target.getName() + " para §5" + ItemBuilder.captalise(gameMode.name()));
		}
	}
	
	@Command(name = "ss", aliases = "screeshare", groupToUse = Group.MODGC)
	public void ss(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length != 1) {
			player.sendMessage("§cUse: /" + cmdArgs.getLabel() + " <playerName>");
			return;
		}
		if(args.length == 1) {
			Player target = Bukkit.getPlayerExact(args[0]);
			
			if(target == null) {
				player.sendMessage("§cEste jogador não está online.");
				return;
			}
			
			if(freeze.contains(target)) {
				freeze.remove(target);
				player.sendMessage("§dVocê removeu o freeze do jogador §5" + target.getName());
				target.sendMessage("§dO Freeze foi removido de você! Parabéns, você está possívelmente limpo!");
				return;
			} else {
				freeze.add(target);
			}
			player.sendMessage("§bVocê aplicou o freeze no jogador §e" + target.getName() + " §baplique a ss no mesmo.");
			target.sendMessage("§bFreeze foi aplicado em você! Você não pode fazer nada até o staffer remover o freeze.");
			target.setGameMode(GameMode.ADVENTURE);
		}
	}
	
	@Command(name = "addlanguage", aliases = {"addlang", "addtranslate"}, groupToUse = Group.NORMAL)
	public void addlang(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /" + cmdArgs.getLabel() + " <language> <key> <text>");
			return;
		}
		if(args.length >= 2) {
			Language language = Language.getLanguage(args[0]);
			if(language == null) {
				player.sendMessage("§cEste idioma não foi encontrado.");
				return;
			}
			String key = args[1];
			String text = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ");
			
			new LanguageManager().addTextToLang(key, text, language);
			player.sendMessage("§aA chave de mensagem " + key + " que foi definida como §f" + text + ". §aPara o idioma " + language.getName() + " §7(ID: " + language.getId() + ") §afoi alterada com sucesso!");
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "ban", groupToUse = Group.TRIAL)
	public void ban(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		GroupManager groupManager = new GroupManager();
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /ban <playerName> <reason>");
			return;
		}
		
		if(args.length == 1) {
			String reason = PunishManager.defaultReason;
			Player target = Bukkit.getPlayerExact(args[0]);
			
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!new GroupManager().exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				if(groupManager.hasGroupPermission(offlinePlayer.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
					player.sendMessage("§cVocê não pode banir este jogador.");
					return;
				}
				
				if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(offlinePlayer.getUniqueId()))) {
					player.sendMessage("§cVocê não pode banir este jogador.");
					return;
				}
				PunishManager offlinePunish = new PunishManager(Types.BAN, Durations.PERMANENT, offlinePlayer, reason, 0L);
				if(offlinePunish.isPunished()) {
					if(offlinePunish.getType().equals(Types.BAN)) {
						if(offlinePunish.getDuration().equals(Durations.PERMANENT)) {
							player.sendMessage("§cEste jogador já está banido permanentemente.");
							return;
						} else {
							if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
								player.sendMessage("§cEste jogador já está banido.");
								return;
							}
							offlinePunish.unPunish();
							offlinePunish.punish(player);
							player.sendMessage("§cEste jogador já possuia uma punição temporária anteriormente, que foi substituída.");
						}
					}
				}
				offlinePunish.unPunish();
				offlinePunish.punish(player);
				for(Player online : Bukkit.getOnlinePlayers()) {
					if(groupManager.hasGroupPermission(online.getUniqueId(), Group.TRIAL)) {
						online.sendMessage("§cO jogador §f" + offlinePlayer.getName() + "(" + offlinePlayer.getUniqueId() + ") §cfoi banido permanentemente pelo staffer §f" + player.getName() + "§c.");
					} else {
						online.sendMessage("§cO jogador §f" + offlinePlayer.getName() + " §cfoi banido do servidor.");
					}
				}
				return;
			}
			if(groupManager.hasGroupPermission(target.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
				player.sendMessage("§cVocê não pode banir este jogador.");
				return;
			}
			
			if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(target.getUniqueId()))) {
				player.sendMessage("§cVocê não pode banir este jogador.");
				return;
			}
			PunishManager punishManager = new PunishManager(Types.BAN, Durations.PERMANENT, target, reason, 0L);
			if(punishManager.isPunished()) {
				if(punishManager.getType().equals(Types.BAN)) {
					if(punishManager.getDuration().equals(Durations.PERMANENT)) {
						player.sendMessage("Este jogador já está banido permanentemente.");
						return;
					} else {
						if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
							player.sendMessage("Este jogador já está banido.");
							return;
						}
						punishManager.unPunish();
						punishManager.punish(player);
						player.sendMessage("Este jogador já possuia uma puniçăo temporária anteriormente, que foi substituida.");
					}
				}
			}
			punishManager.unPunish();
			punishManager.punish(player);
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(groupManager.hasGroupPermission(online.getUniqueId(), Group.TRIAL)) {
					online.sendMessage("§cO jogador §f" + target.getName() + "(" + target.getUniqueId() + ") §cfoi banido permanentemente pelo staffer §f" + player.getName() + "§c.");
				} else {
					online.sendMessage("§cO jogador §f" + target.getName() + " §cfoi banido do servidor.");
				}
			}
			return;
		}
		if(args.length >= 2) {
			String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
			Player target = Bukkit.getPlayerExact(args[0]);
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!new GroupManager().exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				if(groupManager.hasGroupPermission(offlinePlayer.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
					player.sendMessage("§cVocê não pode banir este jogador.");
					return;
				}
				
				if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(offlinePlayer.getUniqueId()))) {
					player.sendMessage("§cVocê não pode banir este jogador.");
					return;
				}
				PunishManager offlinePunish = new PunishManager(Types.BAN, Durations.PERMANENT, offlinePlayer, reason, 0L);
				if(offlinePunish.isPunished()) {
					if(offlinePunish.getType().equals(Types.BAN)) {
						if(offlinePunish.getDuration().equals(Durations.PERMANENT)) {
							player.sendMessage("§cEste jogador já está banido permanentemente.");
							return;
						} else {
							if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
								player.sendMessage("§cEste jogador já está banido.");
								return;
							}
							offlinePunish.unPunish();
							offlinePunish.punish(player);
							player.sendMessage("§cEste jogador já possuia uma punição temporária anteriormente, que foi substituída.");
						}
					}
				}
				offlinePunish.unPunish();
				offlinePunish.punish(player);
				for(Player online : Bukkit.getOnlinePlayers()) {
					if(groupManager.hasGroupPermission(online.getUniqueId(), Group.TRIAL)) {
						online.sendMessage("§cO jogador §f" + offlinePlayer.getName() + "(" + offlinePlayer.getUniqueId() + ") §cfoi banido permanentemente pelo staffer §f" + player.getName() + "§c.");
					} else {
						online.sendMessage("§cO jogador §f" + offlinePlayer.getName() + " §cfoi banido do servidor.");
					}
				}
				return;
			}
			if(groupManager.hasGroupPermission(target.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
				player.sendMessage("§cVocê não pode banir este jogador.");
				return;
			}
			
			if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(target.getUniqueId()))) {
				player.sendMessage("§cVocê não pode banir este jogador.");
				return;
			}
			PunishManager punishManager = new PunishManager(Types.BAN, Durations.PERMANENT, target, reason, 0L);
			if(punishManager.isPunished()) {
				if(punishManager.getType().equals(Types.BAN)) {
					if(punishManager.getDuration().equals(Durations.PERMANENT)) {
						player.sendMessage("§cEste jogador já está banido permanentemente.");
						return;
					} else {
						if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
							player.sendMessage("§cEste jogador já está banido.");
							return;
						}
						punishManager.unPunish();
						punishManager.punish(player);
						player.sendMessage("§cEste jogador já possuia uma puniçăo temporária anteriormente, que foi substituida.");
					}
				}
			}
			punishManager.unPunish();
			punishManager.punish(player);
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(groupManager.hasGroupPermission(online.getUniqueId(), Group.TRIAL)) {
					online.sendMessage("§cO jogador §f" + target.getName() + "(" + target.getUniqueId() + ") §cfoi banido permanentemente pelo staffer §f" + player.getName() + "§c.");
				} else {
					online.sendMessage("§cO jogador §f" + target.getName() + " §cfoi banido do servidor.");
				}
			}
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "unban", groupToUse = Group.TRIAL)
	public void unban(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /unban <playerName>");
			return;
		}
		
		if(args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if (!new GroupManager().exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				PunishManager offlinePunish = new PunishManager(null, null, offlinePlayer, null, 0);
				if(!offlinePunish.isPunished()) {
					player.sendMessage("§cEste jogador não está punido.");
					return;
				} else {
					if(!offlinePunish.getType().equals(Types.BAN)) {
						player.sendMessage("§cEste jogador não está banido.");
						return;
					}
				}
				offlinePunish.unBan();
				player.sendMessage("§eVocê desbaniu " + offlinePlayer.getName() + "(" + offlinePlayer.getUniqueId().toString() + ") com sucesso!");
				return;
			}
			PunishManager punishManager = new PunishManager(null, null, target, null, 0);
			if(!punishManager.isPunished()) {
				player.sendMessage("§cEste jogador não está punido.");
				return;
			} else {
				if(!punishManager.getType().equals(Types.BAN)) {
					player.sendMessage("§cEste jogador não está banido.");
					return;
				}
			}
			punishManager.unBan();
			player.sendMessage("§aVocê desbaniu " + target.getName() + "(" + target.getUniqueId().toString() + ") com sucesso!");
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "tempban", groupToUse = Group.TRIAL)
	public void tempban(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		GroupManager groupManager = new GroupManager();
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /tempban <playerName> <time> [reason]");
			return;
		}
		if(args.length == 2) {
			long expire = 0L;
			try {
				expire = ItemBuilder.fromString(args[1]);
			} catch(Exception exception) {
				player.sendMessage("§cFormato de tempo incorreto. Utilize: 1d,1h,1m,1s.");
				return;
			}
			String reason = PunishManager.defaultReason;
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if (!new GroupManager().exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				if(groupManager.hasGroupPermission(offlinePlayer.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
					player.sendMessage("§cVocê não pode banir este jogador.");
					return;
				}
				
				if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(offlinePlayer.getUniqueId()))) {
					player.sendMessage("§cVocê não pode banir este jogador.");
					return;
				}
				PunishManager offlinePunish = new PunishManager(Types.BAN, Durations.TEMPORARY, offlinePlayer, reason, expire);
				if(offlinePunish.isPunished()) {
					if(offlinePunish.getType().equals(Types.BAN)) {
						player.sendMessage("§cEste jogador já está banido permanentemente.");
						return;
					}
				}
				offlinePunish.unPunish();
				offlinePunish.punish(player);
				return;
			}
			if(groupManager.hasGroupPermission(target.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
				player.sendMessage("§cVocê não pode banir este jogador.");
				return;
			}
			
			if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(target.getUniqueId()))) {
				player.sendMessage("§cVocê não pode banir este jogador.");
				return;
			}
			PunishManager punishManager = new PunishManager(Types.BAN, Durations.TEMPORARY, target, reason, expire);
			if(punishManager.isPunished()) {
				if(punishManager.getType().equals(Types.BAN)) {
					player.sendMessage("§cEste jogador já está banido permanentemente.");
					return;
				}
			}
			punishManager.unPunish();
			punishManager.punish(player);
			return;
		}
		if(args.length >= 3) {
			long expire = 0L;
			try {
				expire = ItemBuilder.fromString(args[1]);
			} catch(Exception exception) {
				player.sendMessage("§cFormato de tempo incorreto. Utilize: 1d,1h,1m,1s.");
				return;
			}
			String reason = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ");
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if (!new GroupManager().exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				if(groupManager.hasGroupPermission(offlinePlayer.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
					player.sendMessage("§cVocê não pode banir este jogador.");
					return;
				}
				
				if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(offlinePlayer.getUniqueId()))) {
					player.sendMessage("§cVocê não pode banir este jogador.");
					return;
				}
				PunishManager offlinePunish = new PunishManager(Types.BAN, Durations.TEMPORARY, offlinePlayer, reason, expire);
				if(offlinePunish.isPunished()) {
					if(offlinePunish.getType().equals(Types.BAN)) {
						player.sendMessage("§cEste jogador já está banido permanentemente.");
						return;
					}
				}
				offlinePunish.unPunish();
				offlinePunish.punish(player);
				return;
			}
			if(groupManager.hasGroupPermission(target.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
				player.sendMessage("§cVocê não pode banir este jogador.");
				return;
			}
			
			if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(target.getUniqueId()))) {
				player.sendMessage("§cVocê não pode banir este jogador.");
				return;
			}
			PunishManager punishManager = new PunishManager(Types.BAN, Durations.TEMPORARY, target, reason, expire);
			if(punishManager.isPunished()) {
				if(punishManager.getType().equals(Types.BAN)) {
					player.sendMessage("§cEste jogador já está banido permanentemente.");
					return;
				}
			}
			punishManager.unPunish();
			punishManager.punish(player);
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "mute", groupToUse = Group.TRIAL)
	public void mute(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		GroupManager groupManager = new GroupManager();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /mute <playerName> [reason]");
			return;
		}
		if(args.length == 1) {
			String reason = PunishManager.defaultReason;
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if (!new GroupManager().exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				if(groupManager.hasGroupPermission(offlinePlayer.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
					player.sendMessage("§cVocê não pode mutar este jogador.");
					return;
				}
				
				if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(offlinePlayer.getUniqueId()))) {
					player.sendMessage("§cVocê não pode mutar este jogador.");
					return;
				}
				PunishManager offlinePunish = new PunishManager(Types.MUTE, Durations.PERMANENT, offlinePlayer, reason, 0L);
				if(offlinePunish.isPunished()) {
					if(offlinePunish.getType().equals(Types.MUTE)) {
						if(offlinePunish.getDuration().equals(Durations.PERMANENT)) {
							player.sendMessage("§cEste jogador já está mutado permanentemente.");
							return;
						} else {
							if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
								player.sendMessage("§cEste jogador já está mutado.");
								return;
							}
							offlinePunish.unPunish();
							offlinePunish.punish(player);
							player.sendMessage("§cEste jogador já possuia uma puniçăo temporária anteriormente, que foi substituida.");
						}
					}
				}
				offlinePunish.unPunish();
				offlinePunish.punish(player);
				return;
			}
			if(groupManager.hasGroupPermission(target.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
				player.sendMessage("§cVocê não pode mutar este jogador.");
				return;
			}
			
			if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(target.getUniqueId()))) {
				player.sendMessage("§cVocê não pode mutar este jogador.");
				return;
			}
			PunishManager punishManager = new PunishManager(Types.MUTE, Durations.PERMANENT, target, reason, 0L);
			if(punishManager.isPunished()) {
				if(punishManager.getType().equals(Types.MUTE)) {
					if(punishManager.getDuration().equals(Durations.PERMANENT)) {
						player.sendMessage("§cEste jogador já está mutado permanentemente.");
						return;
					} else {
						if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
							player.sendMessage("§cEste jogador já está mutado.");
							return;
						}
						punishManager.unPunish();
						punishManager.punish(player);
						player.sendMessage("§cEste jogador já possuia uma puniçăo temporária anteriormente, que foi substituida.");
					}
				}
			}
			punishManager.unPunish();
			punishManager.punish(player);
			return;
		}
		if(args.length >= 2) {
			String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if (!new GroupManager().exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				if(groupManager.hasGroupPermission(offlinePlayer.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
					player.sendMessage("§cVocê não pode mutar este jogador.");
					return;
				}
				
				if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(offlinePlayer.getUniqueId()))) {
					player.sendMessage("§cVocê não pode mutar este jogador.");
					return;
				}
				PunishManager offlinePunish = new PunishManager(Types.MUTE, Durations.PERMANENT, offlinePlayer, reason, 0L);
				if(offlinePunish.isPunished()) {
					if(offlinePunish.getType().equals(Types.MUTE)) {
						if(offlinePunish.getDuration().equals(Durations.PERMANENT)) {
							player.sendMessage("§cEste jogador já está mutado permanentemente.");
							return;
						} else {
							if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
								player.sendMessage("§cEste jogador já está mutado.");
								return;
							}
							offlinePunish.unPunish();
							offlinePunish.punish(player);
							player.sendMessage("§cEste jogador já possuia uma puniçăo temporária anteriormente, que foi substituida.");
						}
					}
				}
				offlinePunish.unPunish();
				offlinePunish.punish(player);
				return;
			}
			PunishManager punishManager = new PunishManager(Types.MUTE, Durations.PERMANENT, target, reason, 0L);
			if(punishManager.isPunished()) {
				if(punishManager.getType().equals(Types.MUTE)) {
					if(punishManager.getDuration().equals(Durations.PERMANENT)) {
						player.sendMessage("§cEste jogador já está mutado permanentemente.");
						return;
					} else {
						if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
							player.sendMessage("§cEste jogador já está mutado.");
							return;
						}
						punishManager.unPunish();
						punishManager.punish(player);
						player.sendMessage("§cEste jogador já possuia uma puniçăo temporária anteriormente, que foi substituida.");
					}
				}
			}
			punishManager.unPunish();
			punishManager.punish(player);
			return;
		}
 	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "unmute", groupToUse = Group.TRIAL)
	public void unmute(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		GroupManager groupManager = new GroupManager();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /unmute <playerName>");
			return;
		}
		if(args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if (!groupManager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cEste jogador não existe no servidor.");
					return;
				}
				PunishManager offlinePunish = new PunishManager(null, null, offlinePlayer, null, 0);
				if(!offlinePunish.isPunished()) {
					player.sendMessage("§cEste jogador não está punido.");
					return;
				} else {
					if(!offlinePunish.getType().equals(Types.MUTE)) {
						player.sendMessage("§cEste jogador não está mutado.");
						return;
					}
				}
				offlinePunish.unMute();
				player.sendMessage("§aVocê desmutou " + offlinePlayer.getName() + "(" + offlinePlayer.getUniqueId().toString() + ") com sucesso!");
				return;
			}
			PunishManager punishManager = new PunishManager(null, null, target, null, 0);
			if(!punishManager.isPunished()) {
				player.sendMessage("§cEste jogador não está punido.");
				return;
			} else {
				if(!punishManager.getType().equals(Types.MUTE)) {
					player.sendMessage("§cEste jogador não está mutado.");
					return;
				}
			}
			punishManager.unMute();
			player.sendMessage("§aVocê desmutou " + target.getName() + "(" + target.getUniqueId().toString() + ") com sucesso!");
			return;
		}
	}
	
	@Command(name = "kick", groupToUse = Group.YOUTUBERPLUS)
	public void kick(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		GroupManager groupManager = new GroupManager();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /kick <playerName> [reason]");
			return;
		}
		if(args.length == 1) {
			String reason = PunishManager.defaultReason;
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				player.sendMessage("§cEste jogador não está online.");
				return;
			}
			if(groupManager.hasGroupPermission(target.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
				player.sendMessage("§cVocê não pode kickar este jogador.");
				return;
			}
			
			if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(target.getUniqueId()))) {
				player.sendMessage("§cVocê não pode kickar este jogador.");
				return;
			}
			target.kickPlayer("§cVocê foi expulso do servidor pelo motivo:\n" + reason);
			return;
		}
		if(args.length >= 2) {
			String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
			Player target = Bukkit.getPlayerExact(args[0]);
			if(target == null) {
				player.sendMessage("§cEste jogador não está online.");
				return;
			}
			if(groupManager.hasGroupPermission(target.getUniqueId(), Group.YOUTUBERPLUS) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
				player.sendMessage("§cVocê não pode kickar este jogador.");
				return;
			}
			
			if(!groupManager.hasGroupPermission(player.getUniqueId(), groupManager.getGroup(target.getUniqueId()))) {
				player.sendMessage("§cVocê não pode kickar este jogador.");
				return;
			}
			target.kickPlayer("§cVocê foi expulso do servidor pelo motivo:\n" + reason);
			return;
		}
	}

}
