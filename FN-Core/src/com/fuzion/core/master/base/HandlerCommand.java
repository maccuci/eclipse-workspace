package com.fuzion.core.master.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class HandlerCommand extends Command {
	
	public boolean enabled = true;
	
	public HandlerCommand(String name) {
		super(name);
	}

	public HandlerCommand(String name, String description) {
		super(name, description, "", new ArrayList<String>());
	}

	public HandlerCommand(String name, String description, List<String> aliases) {
		super(name, description, "", aliases);
	}

	public HandlerCommand(String name, String description, String... aliases) {
		super(name, description, "", Arrays.asList(aliases));
	}

	public abstract boolean execute(CommandSender commandSender, String label, String[] args);
	
	public Integer getInteger(String string) {
		return Integer.valueOf(string);
	}

	public boolean isPlayer(CommandSender commandSender) {
		boolean isPlayer = commandSender instanceof Player;
		if (!isPlayer)
			commandSender.sendMessage("ERRO: Somente players podem usar esse comando.");
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
}
