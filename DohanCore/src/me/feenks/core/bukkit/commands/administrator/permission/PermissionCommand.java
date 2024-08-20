package me.feenks.core.bukkit.commands.administrator.permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import lombok.Getter;
import me.feenks.core.bukkit.BukkitMain;
import me.feenks.core.bukkit.commands.base.BukkitCommand;

public class PermissionCommand extends BukkitCommand {
	
	public PermissionCommand() {
		super("permission", "Modify player permissions.");
	}
	
	public boolean execute(CommandSender commandSender, String label, String[] args) {
		if (!hasPermission(commandSender, "permission")) {
			return false;
		}
		if(args.length == 0) {
			sendHelp(commandSender);
		} else if (args.length == 1) {
			sendHelp(commandSender);
		} else if (args.length == 2) {
			Bukkit.getScheduler().runTaskAsynchronously(BukkitMain.getPlugin(), new PermissionListAsync(commandSender, args));
		}
		return false;
	}
	
	public boolean validString(String str) {
		return (str.matches("[a-zA-Z0-9_]+")) && str.length() >= 2 && str.length() <= 6;
	}
	
	private void sendHelp(CommandSender commandSender) {
		commandSender.sendMessage("§c/permission add/remove <nick/uuid> <permission> >time> §f- §7adicione/remova uma permissão de um player.");
		commandSender.sendMessage("§c/permission list <nick/uuid> §f- §7veja as permissões de um player.");
	}
	
	private final class PermissionListAsync implements Runnable {
		
		@Getter
		private final CommandSender commandSender;
		private final String[] args;

		private PermissionListAsync(CommandSender sender, String[] args) {
			this.commandSender = sender;
			this.args = args;
		}
		
		public void run() {
			if (args[0].equalsIgnoreCase("list")) {
				String name = args[1];
				
				if(name == null) {
					return;
				}
			}
		}
	}
}
