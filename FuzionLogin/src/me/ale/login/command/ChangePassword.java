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
				info.getCommandSender().sendMessage("�cVoc� � um jogador original.");
				return;
			}
		} catch (InvalidCheckException e) {
			e.printStackTrace();
		}

		if (!Bukkit.useMySQL()) {
			if (!Bukkit.getStorage().needLogin(info.getCommandSender().getName())) {
				if (info.getArgs().length == 1 || info.getArgs().length == 0) {
					info.getCommandSender().sendMessage("�cMude sua senha usando: /changepassword <currentPassword> <newPassword>");
					return;
				} else if (info.getArgs().length == 2) {
					if (info.getArgs()[1].length() > 16) {
						info.getCommandSender().sendMessage("�cSua senha � muito grande.");
						return;
					}
					if (Util.decode(ConfigType.DATA.getConfig()
							.getString("AUTH." + info.getCommandSender().getName() + ".PASSWORD")) == null) {
						info.getCommandSender().sendMessage("�cVoc� n�o pode mudar sua senha.");
						return;
					}
					if (info.getArgs()[0].equals(Util.decode(ConfigType.DATA.getConfig()
							.getString("AUTH." + info.getCommandSender().getName() + ".PASSWORD")))) {
						if (info.getArgs()[0].equals(info.getArgs()[1])) {
							info.getCommandSender().sendMessage("�cSua senha atual � igual a esta.");
						} else {
							ConfigType.DATA.getConfig().set("AUTH." + info.getCommandSender().getName() + ".PASSWORD",
									new String(Util.encode(info.getArgs()[1])));
							try {
								ConfigType.DATA.getConfig().save(Config.getPaths()[2]);
							} catch (IOException e) {
								Util.severe("N�o foi poss�vel salvar a config.");
								e.printStackTrace();
							}
							Bukkit.getPlugin().getServer().getPluginManager().callEvent(new PlayerChangePasswordEvent(
									(Player) info.getCommandSender(), info.getArgs()[0], info.getArgs()[1]));
							info.getCommandSender().sendMessage("�aSua senha foi alterada.");
						}
					} else {
						info.getCommandSender().sendMessage("�cSua senha atual est� errada.");
					}
				} else if (info.getArgs().length > 2) {
					info.getCommandSender().sendMessage("�cSua nova senha n�o pode ter espa�os.");
				}
			} else {
				info.getCommandSender().sendMessage("�cVoc� precisa se logar primeiro.");
			}
		} else {
			if (!Bukkit.getStorage().needLogin(info.getCommandSender().getName())) {
				if (info.getArgs().length == 1 || info.getArgs().length == 0) {
					info.getCommandSender().sendMessage("�cMude sua senha usando: /changepassword <currentPassword> <newPassword>");
					return;
				} else if (info.getArgs().length == 2) {
					if (info.getArgs()[1].length() > 16) {
						info.getCommandSender().sendMessage("�cSua senha � muito grande.");
						return;
					}
					if (Util.decode(ConfigType.DATA.getConfig()
							.getString("AUTH." + info.getCommandSender().getName() + ".PASSWORD")) == null) {
						info.getCommandSender().sendMessage("�cVoc� n�o pode mudar sua senha.");
						return;
					}
					if (info.getArgs()[0].equals(
							Util.decode(Bukkit.getSQLManager().getPassword(info.getCommandSender().getName())))) {
						if (info.getArgs()[0].equals(info.getArgs()[1])) {
							info.getCommandSender().sendMessage("�cSua senha atual � igual a esta.");
						} else {
							Bukkit.getSQLManager().updatePassword(info.getCommandSender().getName(),
									Util.encode(info.getArgs()[1]));
							Bukkit.getPlugin().getServer().getPluginManager().callEvent(new PlayerChangePasswordEvent(
									(Player) info.getCommandSender(), info.getArgs()[0], info.getArgs()[1]));
							info.getCommandSender().sendMessage("�aSua senha foi alterada.");
						}
					} else {
						info.getCommandSender().sendMessage("�cSua senha atual est� errada.");
					}
				} else if (info.getArgs().length > 2) {
					info.getCommandSender().sendMessage("�cSua nova senha n�o pode ter espa�os.");
				}
			} else {
				info.getCommandSender().sendMessage("�cVoc� precisa se logar primeiro.");
			}
		}
	}

}
