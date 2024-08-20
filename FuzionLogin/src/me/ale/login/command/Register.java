package me.ale.login.command;

import java.io.IOException;

import org.bukkit.entity.Player;

import me.ale.login.Bukkit;
import me.ale.login.check.Check;
import me.ale.login.command.controller.CommandFactory;
import me.ale.login.command.controller.CommandInfo;
import me.ale.login.database.SQLManager.Status;
import me.ale.login.event.PlayerRegisterEvent;
import me.ale.login.exception.InvalidCheckException;
import me.ale.login.util.Config;
import me.ale.login.util.Util;
import me.ale.login.util.Config.ConfigType;

public class Register extends CommandFactory {

	public Register() {
		super("register");
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
						.get("AUTH." + info.getCommandSender().getName() + ".PASSWORD") == null) {
					if (info.getArgs().length == 0) {
						info.getCommandSender().sendMessage("§cRegistre usando: /register <password>");
					} else if (info.getArgs().length == 1) {
						if (info.getArgs().length > 16) {
							info.getCommandSender().sendMessage("§cSua senha é muito grande.");
							return;
						}
						ConfigType.DATA.getConfig().set("AUTH." + info.getCommandSender().getName() + ".PASSWORD",
								new String(Util.encode(info.getArgs()[0])));
						try {
							ConfigType.DATA.getConfig().save(Config.getPaths()[2]);
						} catch (IOException e) {
							Util.severe("Config não salva");
							e.printStackTrace();
						}
						Bukkit.getPlugin().getServer().getPluginManager().callEvent(
								new PlayerRegisterEvent((Player) info.getCommandSender(), info.getArgs()[0]));
						Bukkit.getStorage().removeNeedLogin(info.getCommandSender().getName());
						info.getCommandSender().sendMessage("§aVocê se registrou com sucesso.");
					} else if (info.getArgs().length > 1) {
						info.getCommandSender().sendMessage("§cAlgo deu errado, tente novamente.");
					}
				} else {
					info.getCommandSender().sendMessage("§cVocê já está registrado.");
				}
			} else {
				info.getCommandSender().sendMessage("§cVocê já está logado.");
			}
		} else {
			if (Bukkit.getStorage().needLogin(info.getCommandSender().getName())) {
				if (!Bukkit.getSQLManager().hasOnDatabase(info.getCommandSender().getName())) {
					if (info.getArgs().length == 0) {
						info.getCommandSender().sendMessage("§cRegistre usando: /register <password>");
					} else if (info.getArgs().length == 1) {
						if (info.getArgs().length > 16) {
							info.getCommandSender().sendMessage("§cSua senha é muito grande.");
							return;
						}
						Bukkit.getSQLManager().setPasswordAndStatus(info.getCommandSender().getName(), Status.CRACKED,
								new String(Util.encode(info.getArgs()[0])));
						Bukkit.getPlugin().getServer().getPluginManager().callEvent(
								new PlayerRegisterEvent((Player) info.getCommandSender(), info.getArgs()[0]));
						Bukkit.getStorage().removeNeedLogin(info.getCommandSender().getName());
						info.getCommandSender().sendMessage("§aVocê se registrou com sucesso.");
					} else if (info.getArgs().length > 1) {
						info.getCommandSender().sendMessage("§cAlgo deu errado, tente novamente.");
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
