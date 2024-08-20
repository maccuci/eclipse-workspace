package com.fuzion.core.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.AccountManager;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;

public class GroupCommand implements CommandClass {
	
	@SuppressWarnings("deprecation")
	@Command(name = "group", aliases = "groupset", groupToUse = Group.ADMIN)
	public void group(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		GroupManager groupManager = new GroupManager();
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /" + cmdArgs.getLabel() + " <player> <group> [time]");
			return;
		}
		
		if(args.length == 2) {
			Group group = Group.NORMAL;
			try {
				group = Group.valueOf(args[1].toUpperCase());
			} catch(Exception exception) {
				player.sendMessage("§cEste grupo não existe.");
				return;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!groupManager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cJogador inexistente no banco de dados.");
					return;
				}
				if(!groupManager.hasGroupPermission(offlinePlayer.getUniqueId(), groupManager.getGroup(offlinePlayer.getUniqueId()))) {
					player.sendMessage("§cVocê não pode alterar o grupo desse jogador.");
					return;
				}
				if(!groupManager.hasGroupPermission(player.getUniqueId(), group)) {
					player.sendMessage("§cVocê não pode manejar o grupo " + group.getColor() + group.name() + "!");
					return;
				}
				if(groupManager.hasGroup(player.getUniqueId(), group)) {
					player.sendMessage("§cO jogador " + offlinePlayer.getName() + " já possui o grupo " + group.getColor() + group.name() + "!");
					return;
				}
/*				groupManager.set(offlinePlayer.getUniqueId(), "playergroup", group.name());
				groupManager.set(offlinePlayer.getUniqueId(), "temporary", 0);
				groupManager.set(offlinePlayer.getUniqueId(), "expire", 0L);*/
				AccountManager.getAccount(offlinePlayer.getUniqueId()).setGroup(group);
				player.sendMessage("§eGrupo de " + offlinePlayer.getName() + "(" + offlinePlayer.getUniqueId().toString() + ") alterado para " + group.getColor() + group.name() + " §ecom sucesso.");
				return;
			}
			if(!groupManager.exists(target.getUniqueId())) {
				player.sendMessage("§cJogador inexistente no banco de dados.");
				return;
			}
			if(!groupManager.hasGroupPermission(target.getUniqueId(), groupManager.getGroup(target.getUniqueId()))) {
				player.sendMessage("§cVocê não pode alterar o grupo desse jogador.");
				return;
			}
			if(!groupManager.hasGroupPermission(player.getUniqueId(), group)) {
				player.sendMessage("§cVocê não pode manejar o grupo " + group.getColor() + group.name() + "!");
				return;
			}
			if(groupManager.hasGroup(player.getUniqueId(), group)) {
				player.sendMessage("§cO jogador " + target.getName() + " já possui o grupo " + group.getColor() + group.name() + "!");
				return;
			}
	/*		groupManager.set(target.getUniqueId(), "playergroup", group.name());
			groupManager.set(target.getUniqueId(), "temporary", 0);
			groupManager.set(target.getUniqueId(), "expire", 0L);*/
			AccountManager.getAccount(target).setGroup(group);
			target.kickPlayer("§aVocê recebeu o grupo " + group.getColor() + group.name() + " §acom sucesso!\n§6Entre no servidor novamente para recebe-lo.");
			player.sendMessage("§eGrupo de " + target.getName() + "(" + target.getUniqueId().toString() + ") alterado para " + group.getColor() + group.name() + " §ecom sucesso.");
		}
		if(args.length == 3) {
			Group group = Group.NORMAL;
			try {
				group = Group.valueOf(args[1].toUpperCase());
			} catch(Exception exception) {
				player.sendMessage("§cEste grupo não existe.");
				return;
			}
			if(group.equals(Group.NORMAL)) {
				player.sendMessage("§cEste grupo năo pode ser setado temporáriamente.");
				return;
			}
			String timeString = args[2];
			long time = 0L;
			try {
				time = ItemBuilder.fromString(timeString);
			} catch(Exception exception) {
				player.sendMessage("§cFormato de tempo incorreto. Utilize: 1d,1h,1m,1s.");
				time = 0L;
				return;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!groupManager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cJogador inexistente no banco de dados.");
					return;
				}
				if(!groupManager.hasGroupPermission(offlinePlayer.getUniqueId(), groupManager.getGroup(offlinePlayer.getUniqueId()))) {
					player.sendMessage("§cVocê não pode alterar o grupo desse jogador.");
					return;
				}
				if(!groupManager.hasGroupPermission(player.getUniqueId(), group)) {
					player.sendMessage("§cVocê não pode manejar o grupo " + group.getColor() + group.name() + "!");
					return;
				}
				if(groupManager.hasGroup(player.getUniqueId(), group)) {
					player.sendMessage("§cO jogador " + offlinePlayer.getName() + " já possui o grupo " + group.getColor() + group.name() + "!");
					return;
				}
/*				groupManager.set(offlinePlayer.getUniqueId(), "playergroup", group.name());
				groupManager.set(offlinePlayer.getUniqueId(), "temporary", 1);
				groupManager.set(offlinePlayer.getUniqueId(), "expire", time);*/
				AccountManager.getAccount(offlinePlayer.getUniqueId()).setGroup(group, time);
				player.sendMessage("§eGrupo de " + offlinePlayer.getName() + "(" + offlinePlayer.getUniqueId().toString() + ") alterado para " + group.getColor() + group.name() + " §etemporariamente com sucesso.");
				return;
			}
			if(!groupManager.exists(target.getUniqueId())) {
				player.sendMessage("§cJogador inexistente no banco de dados.");
				return;
			}
			if(!groupManager.hasGroupPermission(target.getUniqueId(), groupManager.getGroup(target.getUniqueId()))) {
				player.sendMessage("§cVocê não pode alterar o grupo desse jogador.");
				return;
			}
			if(!groupManager.hasGroupPermission(player.getUniqueId(), group)) {
				player.sendMessage("§cVocê não pode manejar o grupo " + group.getColor() + group.name() + "!");
				return;
			}
			if(groupManager.hasGroup(player.getUniqueId(), group)) {
				player.sendMessage("§cO jogador " + target.getName() + " já possui o grupo " + group.getColor() + group.name() + "!");
				return;
			}
/*			groupManager.set(target.getUniqueId(), "playergroup", group.name());
			groupManager.set(target.getUniqueId(), "temporary", 1);
			groupManager.set(target.getUniqueId(), "expire", time);*/
			AccountManager.getAccount(target.getUniqueId()).setGroup(group, time);
			target.kickPlayer("§aVocê recebeu o grupo " + group.getColor() + group.name() + " §atemporariamente com sucesso!\n§6Entre no servidor novamente para recebe-lo.");
			player.sendMessage("§eGrupo de " + target.getName() + "(" + target.getUniqueId().toString() + ") alterado para " + group.getColor() + group.name() + " §ecom sucesso.");
		}
	}
}
