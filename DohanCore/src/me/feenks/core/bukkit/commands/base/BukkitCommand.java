package me.feenks.core.bukkit.commands.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public abstract class BukkitCommand extends Command {
	
	public boolean enable = true;
	
	public BukkitCommand(String name) {
		super(name);
	}

	public BukkitCommand(String name, String description) {
		super(name, description, "", new ArrayList<String>());
	}

	public BukkitCommand(String name, String description, List<String> aliases) {
		super(name, description, "", aliases);
	}

	public BukkitCommand(String name, String description, String... aliases) {
		super(name, description, "", Arrays.asList(aliases));
	}
	
	public abstract boolean execute(CommandSender commandSender, String label, String[] args);

	public Integer getInteger(String string) {
		return Integer.valueOf(string);
	}

	public boolean isPlayer(CommandSender commandSender) {
		boolean isPlayer = commandSender instanceof Player;
		if (!isPlayer)
			sendExecutorMessage(commandSender);
		return isPlayer;
	}

	public boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public boolean isUUID(String string) {
		try {
			UUID.fromString(string);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean hasPermission(CommandSender commandSender, String perm) {
		boolean hasPermission = commandSender.hasPermission("core.cmd." + perm);
		if (!hasPermission)
			sendPermissionMessage(commandSender);
		return hasPermission;
	}

	public String getArgs(String[] args, int starting) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = starting; i < args.length; i++) {
			stringBuilder.append(args[i] + " ");
		}
		return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
	}

	public void sendNumericMessage(CommandSender commandSender) {
		commandSender.sendMessage("§cVocê pode inserir apenas números.");
	}

	public void sendPermissionMessage(CommandSender commandSender) {
		commandSender.sendMessage("§cVocê não possui permissão para utilizar este comando.");
	}

	public void sendExecutorMessage(CommandSender commandSender) {
		commandSender.sendMessage("ERRO: Somente players podem usar esse comando.");
	}

	public void sendArgumentMessage(CommandSender commandSender, String command, String args) {
		Random random = new Random();
		int randomColor = random.nextInt(9);

		commandSender.sendMessage("§" + randomColor + command.toUpperCase() + ChatColor.RESET + " Use: §f" + args);

	}

	public void sendofflinePlayerMessage(CommandSender commandSender, String player) {
		commandSender.sendMessage("§cO jogador " + player + " está offline.");
	}
}
