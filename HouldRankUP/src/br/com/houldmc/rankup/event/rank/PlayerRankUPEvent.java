package br.com.houldmc.rankup.event.rank;

import org.bukkit.entity.Player;

import br.com.houldmc.rankup.event.CustomEvent;
import br.com.houldmc.rankup.manager.rank.list.RankList;
import lombok.Getter;

@Getter
public class PlayerRankUPEvent extends CustomEvent {
	
	private Player player;
	private RankList oldRank, newRank;
	
	public PlayerRankUPEvent(Player player, RankList oldRank, RankList newRank) {
		this.player = player;
		this.oldRank = oldRank;
		this.newRank = newRank;
	}

}
