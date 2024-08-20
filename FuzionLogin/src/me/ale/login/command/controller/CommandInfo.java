package me.ale.login.command.controller;

import org.bukkit.command.CommandSender;


public interface CommandInfo {

	public CommandSender getCommandSender();

	public boolean isPlayer();

	public String[] getArgs();

}
