package br.com.houldmc.rankup.manager.rank;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.event.rank.PlayerRankUPEvent;
import br.com.houldmc.rankup.manager.rank.list.RankList;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;

public class RankManager {
	
	public void rankup(UUID uniqueId, RankList rank) {
		Player player = Bukkit.getPlayer(uniqueId);
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		PlayerRankUPEvent event = new PlayerRankUPEvent(player, getRank(player), rank);
		
		rankUPPlayer.setRank(rank);
		try {
			Main.getPlugin().getManager().getMySQL().getConnection().createStatement().execute("UPDATE `players` SET rank = '" + rank.name() + "' WHERE `uniqueId` = '" + uniqueId.toString() + "';");
		} catch (Exception e) {
			// TODO: handle exception
		}
		Bukkit.getPluginManager().callEvent(event);
	}
	
	public RankList getRank(Player player) {
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		RankList rank = rankUPPlayer.getRank();
		
		return rank;
	}
	
	public RankList getNextRank(Player player) {
		ArrayList<RankList> list = new ArrayList<>();
		for(RankList ranks : RankList.values()) {
			if(!list.contains(ranks)) {
				list.add(ranks);
			}
		}
		return list.get(getRank(player).ordinal() - 1);
	}
	
	public String getRankName(Player player) {
		return getRank(player).getTag();
	}
}
