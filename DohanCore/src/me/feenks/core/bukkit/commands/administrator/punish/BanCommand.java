package me.feenks.core.bukkit.commands.administrator.punish;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.feenks.core.bukkit.BukkitMain;
import me.feenks.core.bukkit.commands.base.BukkitCommand;

public class BanCommand extends BukkitCommand {
	
	public BanCommand() {
		super("ban");
	}
	
	@Override
	public boolean execute(CommandSender commandSender, String label, String[] args) {
		if (!hasPermission(commandSender, "ban")) {
			return false;
		}
		if(args.length == 0) {
			sendHelp(commandSender);
		} else if(args.length == 1) {
			sendHelp(commandSender);
		} else if(args.length >= 2) {
			Bukkit.getScheduler().runTaskAsynchronously(BukkitMain.getPlugin(), new AsyncBanSet(commandSender, args));
		}
		return false;
	}
	
	private void sendHelp(CommandSender commandSender) {
		commandSender.sendMessage("§c/ban <player> <reason> §f §7- Aplique uma punição em algum jogador desejado.");
	}
	
	private final class AsyncBanSet implements Runnable {
		
		@Getter
		private final CommandSender commandSender;
		private final String[] args;

		private AsyncBanSet(CommandSender sender, String[] args) {
			this.commandSender = sender;
			this.args = args;
		}
		
		@Override
		public void run() {
			Player player = Bukkit.getPlayer(args[0]);
			
			if(player == null) {
				//offilineplayer ban
				return;
			}
			//player online ban
		}
	}
}
