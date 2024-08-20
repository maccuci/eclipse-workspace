package com.fuzion.lobby.manager.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.MenuInventory;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.StatsManager;

public class ProfileMenu {
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§0Seu perfil", 3, true);
		GroupManager groupManager = new GroupManager();
		Group group = new GroupManager().getGroup(player.getUniqueId());
		boolean check = group.ordinal() > Group.BETA.ordinal();
		
		menu.setItem(11, new ItemBuilder().type(Material.ENCHANTED_BOOK).name("§aInformações sobre seu grupo").lore("§7Grupo: " + group.getColor() + group.name(), "§7Expira em: §6" + ItemBuilder.fromString(new GroupManager().get(player.getUniqueId(), "expire")), check ? "§7Melhore seu grupo para " + groupManager.getGroup(player.getUniqueId()) == Group.DONO.name() ? "Você é dono!" : groupManager.getNextRank(player.getUniqueId()).getColor() + groupManager.getNextRank(player.getUniqueId()).name() : "").build());
		menu.setItem(13, new ItemBuilder().type(Material.PAPER).name("§aInformações sobre seu status").lore("§7Kills: §e" + new StatsManager().get(player.getUniqueId(), "kills"), "§7Deaths: §e" + new StatsManager().get(player.getUniqueId(), "deaths"), "§7XP: §e" + new StatsManager().get(player.getUniqueId(), "xp"), "§7Wins: §e" + new StatsManager().get(player.getUniqueId(), "wins")).build());
		menu.setItem(14, new ItemBuilder().type(Material.COMPASS).name("§aInformações sobre sua região").lore("§7Estado: Não encontrado.", "§7Cidade: Não encontrada.").build());
		
		menu.open(player);
	}

}
