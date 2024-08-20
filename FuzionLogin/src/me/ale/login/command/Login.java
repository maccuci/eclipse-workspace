package me.ale.login.command;

import org.bukkit.entity.Player;

import me.ale.login.Bukkit;
import me.ale.login.check.Check;
import me.ale.login.command.controller.CommandFactory;
import me.ale.login.command.controller.CommandInfo;
import me.ale.login.event.PlayerLoginEvent;
import me.ale.login.exception.InvalidCheckException;
import me.ale.login.util.Util;
import me.ale.login.util.Config.ConfigType;


public class Login extends CommandFactory {

	public Login() {
		super("login");
	}

	@Override
	protected void onCommand(CommandInfo info) {
		if (!info.isPlayer()) {
			Util.info("");
			return;
		}

		try {
			if (Check.fastCheck(info.getCommandSender().getName())) {
				info.getCommandSender().sendMessage("§cVocê é um jogador original.");
				return;
			}
		} catch (InvalidCheckException e) {
			e.printStackTrace();
		}

		if (!Bukkit.useMySQL()) {
			if (Bukkit.getStorage().needLogin(info.getCommandSender().getName())) {
				if (ConfigType.DATA.getConfig()
						.get("AUTH." + info.getCommandSender().getName() + ".PASSWORD") != null) {
					if (info.getArgs().length == 0) {
						info.getCommandSender().sendMessage("§cLogue usando: /login <password>");
					} else if (info.getArgs().length == 1) {
						if (info.getArgs()[0].equals(Util.decode(ConfigType.DATA.getConfig()
								.getString("AUTH." + info.getCommandSender().getName() + ".PASSWORD")))) {
							Bukkit.getPlugin().getServer().getPluginManager().callEvent(
									new PlayerLoginEvent((Player) info.getCommandSender(), info.getArgs()[0]));
							Bukkit.getStorage().removeNeedLogin(info.getCommandSender().getName());
							info.getCommandSender().sendMessage("§aVocê logou com sucesso.");
						} else {
							info.getCommandSender().sendMessage("§cSua senha esta errada.");
						}
					} else if (info.getArgs().length > 1) {
						info.getCommandSender().sendMessage("§cSua senha não pode conter espaços.");
					}
				} else {
					info.getCommandSender().sendMessage("§cVocê já está registrado.");
				}
			} else {
				info.getCommandSender().sendMessage("§cVocê já está logado.");
			}
		} else {
			if (Bukkit.getStorage().needLogin(info.getCommandSender().getName())) {
				if (Bukkit.getSQLManager().hasOnDatabase(info.getCommandSender().getName())) {
					if (info.getArgs().length == 0) {
						info.getCommandSender().sendMessage("§cLogue usando: /login <password>");
					} else if (info.getArgs().length == 1) {
						if (Util.decode(Bukkit.getSQLManager().getPassword(info.getCommandSender().getName()))
								.equals(info.getArgs()[0])) {
							Bukkit.getPlugin().getServer().getPluginManager().callEvent(
									new PlayerLoginEvent((Player) info.getCommandSender(), info.getArgs()[0]));
							Bukkit.getStorage().removeNeedLogin(info.getCommandSender().getName());
							info.getCommandSender().sendMessage("§aVocê logou com sucesso.");
						} else {
							info.getCommandSender().sendMessage("§cSua senha esta errada.");
						}
					} else if (info.getArgs().length > 1) {
						info.getCommandSender().sendMessage("§cSua senha não pode conter espaços.");
					}
				} else {
					info.getCommandSender().sendMessage("§cVocê já está registrado.");
				}
			} else {
				info.getCommandSender().sendMessage("§cVocê já está logado.");
			}
		}
	}

}
