package br.com.houldmc.rankup.player.listener;

import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;
import br.com.houldmc.rankup.player.account.crud.RankUPPlayerCrud;
import br.com.houldmc.rankup.player.group.GroupManager;
import br.com.houldmc.rankup.player.group.list.GroupList;
import br.com.houldmc.rankup.player.punish.PunishRecord;
import br.com.houldmc.rankup.player.punish.PunishType;

public class AccountListener implements Listener {
	
	public ReentrantLock reentrantLock = new ReentrantLock();
	
	@EventHandler(priority = EventPriority.LOWEST)
	void login(AsyncPlayerPreLoginEvent event) {
		reentrantLock.lock();
		
		RankUPPlayer rankUPPlayer = new RankUPPlayer(event.getUniqueId());
		
		RankUPPlayerManager.accounts.put(event.getUniqueId(), rankUPPlayer);
		new RankUPPlayerCrud().create(rankUPPlayer);
		
		reentrantLock.unlock();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	void login(PlayerLoginEvent event) {
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(event.getPlayer().getUniqueId());
		RankUPPlayerCrud rankUPPlayerCrud = new RankUPPlayerCrud();
		GroupManager groupManager = new GroupManager();
		
		if(rankUPPlayer == null) {
			event.setKickMessage("§cOcorreu um problema ao tentar carregar sua conta.");
			event.setResult(Result.KICK_OTHER);
			return;
		}
		
		if(!rankUPPlayer.canJoin()) {
			PunishRecord record = RankUPPlayerCrud.getLastActiveBan();
			if (record.getType().equals(PunishType.BAN)) {
				event.setKickMessage("§6§lHould§f§lMC §7» §cAdverte\n§cVocê foi banido permanentemente na data §c" + ItemBuilder.formatDate(record.getStart()) + "\n§cpelo motivo \n§f" + record.getMotive());
			} else {
				event.setKickMessage("§6§lHould§f§lMC §7» §cAdverte\n§cVocê foi banido temporariamente na data §c" + ItemBuilder.formatDate(record.getStart()) + "\n§cpelo motivo \n§f" + record.getMotive() + "\n§cExpira em §f" + ItemBuilder.compareSimpleTime(record.getExpire()));
			}
			event.setResult(Result.KICK_BANNED);
		}
		
		rankUPPlayerCrud.update(rankUPPlayer);
		
		if (event.getResult() == Result.KICK_FULL) {
			if (!groupManager.hasGroupPermission(event.getPlayer(), GroupList.MVP)) {
				event.disallow(Result.KICK_OTHER,
						"§6§lWhitelist §7» §fO servidor está lotado!\n Compre §6VIP§f em §bhouldmc.com.br/loja§f para entrar ou tente novamente em §cBreve!");
			} else {
				event.allow();
			}
		}

		if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
			if (groupManager.hasGroupPermission(event.getPlayer(), GroupList.MODERATOR)) {
				event.setResult(PlayerLoginEvent.Result.ALLOWED);
			} else {
				event.disallow(Result.KICK_OTHER, "§6§lWhitelist §7» §fO servidor está com acesso §c§lRESTRITO");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		RankUPPlayerCrud rankUPPlayerCrud = new RankUPPlayerCrud();
		
		rankUPPlayerCrud.update(rankUPPlayer);
		
		if (player.getAddress().getAddress().toString().startsWith("0.0")){
			player.kickPlayer("Tentativa de invasão.");
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void quit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		RankUPPlayerManager rankUPPlayerManager = new RankUPPlayerManager();
		
		rankUPPlayerManager.unloadAccount(player);
	}
}
