package br.com.houldmc.rankup.player.account;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import br.com.houldmc.rankup.player.account.crud.RankUPPlayerCrud;
import lombok.Getter;

public class RankUPPlayerManager {
	
	@Getter
	public static final Map<UUID, RankUPPlayer> accounts = new HashMap<>();
	
	public RankUPPlayer createAccount(Player player) {
		return createAccount(player.getUniqueId());
	}
	
	public RankUPPlayer createAccount(UUID uniqueId) {
		RankUPPlayer rankUPPlayer = new RankUPPlayer(uniqueId);
		RankUPPlayerCrud rankUPPlayerCrud = new RankUPPlayerCrud();
		
		rankUPPlayerCrud.create(rankUPPlayer);
		return rankUPPlayer;
	}
	
	public void unloadAccount(Player player) {
		accounts.remove(player.getUniqueId());
	}
	
	public RankUPPlayer getAccount(Player player) {
		return getAccount(player.getUniqueId());
	}
	
	public RankUPPlayer getAccount(UUID uniqueId) {
		return accounts.get(uniqueId);
	}
}
