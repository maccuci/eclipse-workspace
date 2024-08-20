package me.ale.login.command;

import java.io.IOException;

import org.bukkit.entity.Player;

import me.ale.login.Bukkit;
import me.ale.login.check.Check;
import me.ale.login.command.controller.CommandFactory;
import me.ale.login.command.controller.CommandInfo;
import me.ale.login.event.PlayerChangePasswordEvent;
import me.ale.login.exception.InvalidCheckException;
import me.ale.login.util.Config;
import me.ale.login.util.Util;
import me.ale.login.util.Config.ConfigType;

public class ChangePassword extends CommandFactory {

	public ChangePassword() {
		super("changepassword");
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
			if (!Bukkit.getStorage().needLogin(info.getCommandSender().getName())) {
				if (info.getArgs().length == 1 || info.getArgs().length == 0) {
					info.getCommandSender().sendMessage("§cMude sua senha usando: /changepassword <currentPassword> <newPassword>");
					return;
				} else if (info.getArgs().length == 2) {
					if (info.getArgs()[1].length() > 16) {
						info.getCommandSender().sendMessage("§cSua senha é muito grande.");
						return;
					}
					if (Util.decode(ConfigType.DATA.getConfig()
							.getString("AUTH." + info.getCommandSender().getName() + ".PASSWORD")) == null) {
						info.getCommandSender().sendMessage("§cVocê não pode mudar sua senha.");
						return;
					}
					if (info.getArgs()[0].equals(Util.decode(ConfigType.DATA.getConfig()
							.getString("AUTH." + info.getCommandSender().getName() + ".PASSWORD")))) {
						if (info.getArgs()[0].equals(info.getArgs()[1])) {
							info.getCommandSender().sendMessage("§cSua senha atual é igual a esta.");
						} else {
							ConfigType.DATA.getConfig().set("AUTH." + info.getCommandSender().getName() + ".PASSWORD",
									new String(Util.encode(info.getArgs()[1])));
							try {
								ConfigType.DATA.getConfig().save(Config.getPaths()[2]);
							} catch (IOException e) {
								Util.severe("Não foi possível salvar a config.");
								e.printStackTrace();
							}
							Bukkit.getPlugin().getServer().getPluginManager().callEvent(new PlayerChangePasswordEvent(
									(Player) info.getCommandSender(), info.getArgs()[0], info.getArgs()[1]));
							info.getCommandSender().sendMessage("§aSua senha foi alterada.");
						}
					} else {
						info.getCommandSender().sendMessage("§cSua senha atual está errada.");
					}
				} else if (info.getArgs().length > 2) {
					info.getCommandSender().sendMessage("§cSua nova senha não pode ter espaços.");
				}
			} else {
				info.getCommandSender().sendMessage("§cVocê precisa se logar primeiro.");
			}
		} else {
			if (!Bukkit.getStorage().needLogin(info.getCommandSender().getName())) {
				if (info.getArgs().length == 1 || info.getArgs().length == 0) {
					info.getCommandSender().sendMessage("§cMude sua senha usando: /changepassword <currentPassword> <newPassword>");
					return;
				} else if (info.getArgs().length == 2) {
					if (info.getArgs()[1].length() > 16) {
						info.getCommandSender().sendMessage("§cSua senha é muito grande.");
						return;
					}
					if (Util.decode(ConfigType.DATA.getConfig()
							.getString("AUTH." + info.getCommandSender().getName() + ".PASSWORD")) == null) {
						info.getCommandSender().sendMessage("§cVocê não pode mudar sua senha.");
						return;
					}
					if (info.getArgs()[0].equals(
							Util.decode(Bukkit.getSQLManager().getPassword(info.getCommandSender().getName())))) {
						if (info.getArgs()[0].equals(info.getArgs()[1])) {
							info.getCommandSender().sendMessage("§cSua senha atual é igual a esta.");
						} else {
							Bukkit.getSQLManager().updatePassword(info.getCommandSender().getName(),
									Util.encode(info.getArgs()[1]));
							Bukkit.getPlugin().getServer().getPluginManager().callEvent(new PlayerChangePasswordEvent(
									(Player) info.getCommandSender(), info.getArgs()[0], info.getArgs()[1]));
							info.getCommandSender().sendMessage("§aSua senha foi alterada.");
						}
					} else {
						info.getCommandSender().sendMessage("§cSua senha atual está errada.");
					}
				} else if (info.getArgs().length > 2) {
					info.getCommandSender().sendMessage("§cSua nova senha não pode ter espaços.");
				}
			} else {
				info.getCommandSender().sendMessage("§cVocê precisa se logar primeiro.");
			}
		}
	}

}
