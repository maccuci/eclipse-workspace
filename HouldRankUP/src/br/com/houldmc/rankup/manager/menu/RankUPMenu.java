package br.com.houldmc.rankup.manager.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.api.menu.ClickType;
import br.com.houldmc.rankup.api.menu.MenuClickHandler;
import br.com.houldmc.rankup.api.menu.MenuInventory;
import br.com.houldmc.rankup.api.menu.MenuItem;
import br.com.houldmc.rankup.manager.rank.RankManager;
import br.com.houldmc.rankup.manager.rank.list.RankList;
import br.com.houldmc.rankup.manager.scoreboard.ScoreboardManager;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;
import br.com.houldmc.rankup.player.account.crud.RankUPPlayerCrud;
import lombok.Getter;

public class RankUPMenu {
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§8Rankup", 6);
		ItemBuilder itemBuilder = new ItemBuilder();
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		RankManager rankManager = new RankManager();
		
		for(int i = 0; i < 9; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		}
		
		for(int i = 45; i < 54; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		}
		
		if(rankUPPlayer.getRank() == RankList.EMPEROR) {
			menu.setItem(22, itemBuilder.type(Material.INK_SACK).durability(8).name("§7Evoluir para §8-").build());
			menu.setItem(29, itemBuilder.type(Material.WOOL).durability(14).name("§cCancelar").build());
			menu.setItem(33, itemBuilder.type(Material.BARRIER).name("§cVocê está no último rank.").build());
		} else if(rankUPPlayer.getMoney() >= rankManager.getNextRank(player).getPrice()) {
			menu.setItem(22, itemBuilder.type(Material.INK_SACK).durability(10).name("§7Evoluir para " + rankManager.getNextRank(player).getTag()).build());
			menu.setItem(29, itemBuilder.type(Material.WOOL).durability(14).name("§cCancelar").build());
			menu.setItem(33, new MenuItem(itemBuilder.type(Material.WOOL).durability(5).name("§aEvoluir").lore("§7Você irá gastar §e" + rankManager.getNextRank(player).getPrice() + " moedas §7.").build(), new RankUPPlayerTask(rankUPPlayer, rankManager.getNextRank(player))));
		} else {
			double value = rankManager.getNextRank(player).getPrice() - rankUPPlayer.getMoney();
			menu.setItem(22, itemBuilder.type(Material.INK_SACK).durability(8).name("§7Evoluir para " + rankManager.getNextRank(player).getTag()).build());
			menu.setItem(29, itemBuilder.type(Material.WOOL).durability(14).name("§cCancelar").build());
			menu.setItem(33, itemBuilder.type(Material.WOOL).durability(5).name("§aEvoluir para " + rankManager.getNextRank(player).getTag()).lore(" ", "§7Você precisa de §e" + (ItemBuilder.format(value)) + " moedas §7para evoluir.").build());
		}
		menu.open(player);
	}
	
	public static class RankUPPlayerTask implements MenuClickHandler {
		@Getter private RankUPPlayer rankUPPlayer;
		@Getter private RankList rank;
		
		public RankUPPlayerTask(RankUPPlayer rankUPPlayer, RankList rank) {
			this.rankUPPlayer = rankUPPlayer;
			this.rank = rank;
		}

		@Override
		public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
			p.closeInventory();
			new RankManager().rankup(p.getUniqueId(), rank);
			new RankUPPlayerCrud().update(rankUPPlayer);
			new ScoreboardManager().updateRank(p, rank);
		}
	}
}
