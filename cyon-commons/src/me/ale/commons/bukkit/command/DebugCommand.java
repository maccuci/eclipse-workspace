package me.ale.commons.bukkit.command;

import me.ale.commons.core.account.Rank;
import me.ale.commons.core.command.Command;
import me.ale.commons.core.command.CommandArgs;
import me.ale.commons.core.command.CommandLoader.CommandClass;

public class DebugCommand implements CommandClass {
	
	@Command(name = "raminfo", usage = "/<command>", rankToUse = Rank.OWNER)
	public void ram(CommandArgs cmdArgs) {
		double total = Runtime.getRuntime().maxMemory();
		double free = Runtime.getRuntime().freeMemory();
		double used = total - free;

		double divisor = 1024 * 1024 * 1024;
		double usedPercentage = (used / total) * 100;

		cmdArgs.getSender().sendMessage((total / divisor) + "GB de memoria RAM Maxima");

		cmdArgs.getSender().sendMessage((used / divisor) + "GB de memoria RAM Usada");
		cmdArgs.getSender().sendMessage((free / divisor) + "GB de memoria RAM Livre");
		cmdArgs.getSender().sendMessage(usedPercentage + "% da memoria RAM");
	}

}
