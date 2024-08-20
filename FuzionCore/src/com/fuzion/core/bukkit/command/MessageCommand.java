package com.fuzion.core.bukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.manager.menu.BalanceMenu;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.StatsManager;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.Completer;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;

public class MessageCommand implements CommandClass {
	
	public static boolean chat = true;
	public static List<Player> chatBypass = new ArrayList<>();
	
	public enum Modes {
		ON, OFF, CLEAR, BYPASS;
	}
	
	@Command(name = "chat", groupToUse = Group.YOUTUBERPLUS)
	public void chat(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		GroupManager groupManager = new GroupManager();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /chat <mode> [player]");
		}
		if(args.length == 1) {
			Modes mode = Modes.CLEAR;
			try {
				mode = Modes.valueOf(args[0].toUpperCase());
			} catch(Exception exception) {
				player.sendMessage("§cEste modo não foi encontrado.");
				return;
			}
			if(mode.equals(Modes.BYPASS)) {
				if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
					player.sendMessage("§cVocê não possui permissão.");
					return;
				}
				player.sendMessage("§cUse: /chat <mode> [player]");
				return;
			}
			if(mode.equals(Modes.CLEAR)) {
				for(int i = 0; i < 100; i++) {
					Bukkit.broadcastMessage(" ");
				}
				Bukkit.broadcastMessage("§eO chat foi limpo.");
				return;
			}
			if(mode.equals(Modes.ON)) {
				if(chat) {
					player.sendMessage("§cO Chat já se encontra ativado.");
					return;
				}
				chat = true;
				Bukkit.broadcastMessage("§aO Chat do servidor foi ativado.");
				return;
			}
			if(mode.equals(Modes.OFF)) {
				if(!chat) {
					player.sendMessage("§cO Chat já se encontra desativado.");
					return;
				}
				chat = false;
				Bukkit.broadcastMessage("§eO Chat do servidor foi desativado.");
				return;
			}
		}
		if(args.length == 2) {
			if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.GERENTE)) {
				player.sendMessage("§cVocê não possui permissão.");
				return;
			}
			Player target = Bukkit.getPlayer(args[1]);
			if(target == null) {
				player.sendMessage("§cEste jogador não está online.");
				return;
			}
			if(chatBypass.contains(target)) {
				chatBypass.remove(target);
			} else {
				chatBypass.add(target);
			}
			player.sendMessage(chatBypass.contains(player) ? "§aAgora o jogador " + target.getName() + " pode falar com o chat desativado." : "§cAgora, o jogador " + target.getName() + " não pode mais falar com o chat desativado.");
			return;
		}
	}
	
	@Command(name = "tell", aliases = {"w", "whisper"})
	public void tell(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /" + cmdArgs.getLabel() + " <playerName> <message>");
			return;
		}
		if(args.length >= 2) {
			Player target = Bukkit.getPlayer(args[0]);
			String message = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
			
			if(target == null) {
				player.sendMessage("§cEste jogador não está online.");
				return;
			}
			target.sendMessage("§8[§f" + player.getName() + " §8»§7 " + message + "§8]");
			player.sendMessage("§8[§f" + target.getName() + " §8«§7 " + message + "§8]");
		}
	}
	
	@Command(name = "balance")
	public void balance(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length >= 0) {
			BalanceMenu.open(player);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "coins")
	public void coins(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		StatsManager statsManager = new StatsManager();
		
		if(args.length != 1) {
			player.sendMessage("§cUse: /coins doar <playerName> <amount>");
			return;
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("doar")) {
				Player target = Bukkit.getPlayer(args[1]);

				if (target == null) {
					Integer amount;
					try {
						amount = Integer.valueOf(args[2]);
					} catch (Exception e) {
						player.sendMessage("§cVocê pode doar apenas números.");
						return;
					}
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
					if (!statsManager.exists(offlinePlayer.getUniqueId())) {
						player.sendMessage("§cJogador inexistente no banco de dados.");
						return;
					}

					if (amount <= 100) {
						player.sendMessage("§cO Valor tem que ser maior do que 100 coins.");
						return;
					}

					statsManager.set(offlinePlayer.getUniqueId(), "coins",
							statsManager.get(offlinePlayer.getUniqueId(), "coins") + amount);
					statsManager.set(player.getUniqueId(), "coins",
							statsManager.get(player.getUniqueId(), "coins") - amount);
					return;
				}
				Integer amount;
				try {
					amount = Integer.valueOf(args[2]);
				} catch (Exception e) {
					player.sendMessage("§cVocê pode doar apenas números.");
					return;
				}
				if (!statsManager.exists(target.getUniqueId())) {
					player.sendMessage("§cJogador inexistente no banco de dados.");
					return;
				}

				if (amount <= 100) {
					player.sendMessage("§cO Valor tem que ser maior do que 100 coins.");
					return;
				}

				statsManager.set(target.getUniqueId(), "coins",
						statsManager.get(target.getUniqueId(), "coins") + amount);
				statsManager.set(player.getUniqueId(), "coins",
						statsManager.get(player.getUniqueId(), "coins") - amount);
				player.sendMessage("§eVocê doou " + amount + " coins para o jogador " + target.getName());
				player.sendMessage("§eVocê recebeu uma doação de " + amount + " coins do jogador " + target.getName());
			}
		}
	}
	
	@Completer(name = "chat")
	public List<String> chatcompleter(CommandArgs cmdArgs) {
		String[] args = cmdArgs.getArgs();
		List<String> results = new ArrayList<>();
		String search = args[0].toLowerCase();
		
		if(args.length == 1) {
			for(Modes modes : Modes.values()) {
				if(modes.name().startsWith(search)) {
					results.add(modes.name().toLowerCase());
				}
			}
		}
		return results;
	}
}
