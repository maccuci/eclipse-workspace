package me.ale.pvp.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.League;
import me.ale.commons.core.account.manager.LeagueManager;
import me.ale.commons.core.account.manager.StatsManager;

public class DeathListener implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(!(event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player))
			return;
		
		if(event.getEntity().getType() == EntityType.PLAYER && event.getEntity().getKiller().getType() == EntityType.PLAYER) {
			Player killer = (Player)event.getEntity().getKiller();
			Player killed = (Player)event.getEntity();
			StatsManager stats = BukkitMain.getPlugin().getStatsManager();
			LeagueManager league = new LeagueManager(stats);
			
			if(league.getLeague(killed.getUniqueId()) == League.APPRENTICE) {
				stats.set(killed.getUniqueId(), "deaths", stats.get(killer.getUniqueId(), "deaths") + 1);
				stats.set(killed.getUniqueId(), "money", stats.get(killer.getUniqueId(), "money") - 50);
				if(stats.get(killed.getUniqueId(), "streak") >= 1) {
					stats.set(killed.getUniqueId(), "streak", 0);
				}
				killed.sendMessage("§7Você morreu para §c" + killer.getName());
				killed.sendMessage("§7Deaths: §c" + stats.get(killer.getUniqueId(), "deaths"));
				killed.sendMessage("§7Money: §2" + stats.get(killer.getUniqueId(), "money"));
				return;
			} else {
				stats.set(killed.getUniqueId(), "deaths", stats.get(killer.getUniqueId(), "deaths") + 1);
				stats.set(killed.getUniqueId(), "money", stats.get(killer.getUniqueId(), "money") - 50);
				stats.set(killed.getUniqueId(), "exp", stats.get(killer.getUniqueId(), "exp") - 7);
				if(stats.get(killed.getUniqueId(), "streak") >= 1) {
					stats.set(killed.getUniqueId(), "streak", 0);
				}
				killed.sendMessage("§7Você morreu para §c" + killer.getName());
				killed.sendMessage("§7Deaths: §c" + stats.get(killer.getUniqueId(), "deaths"));
				killed.sendMessage("§7Money: §2" + stats.get(killer.getUniqueId(), "money"));
				killed.sendMessage("§7Exp: §e" + stats.get(killer.getUniqueId(), "exp"));
			}
			
			stats.set(killed.getUniqueId(), "kills", stats.get(killer.getUniqueId(), "kills") + 1);
			stats.set(killed.getUniqueId(), "streak", stats.get(killer.getUniqueId(), "streak") + 1);
			if(stats.get(killer.getUniqueId(), "streak") >= 1 % 0) {
				stats.set(killed.getUniqueId(), "money", stats.get(killer.getUniqueId(), "money") + 50);
			}
			stats.set(killed.getUniqueId(), "exp", stats.get(killer.getUniqueId(), "exp") + 10);
			
			killer.sendMessage("§7Você matou o jogador §a" + killed.getName());
			killer.sendMessage("§7Kills: §a" + stats.get(killer.getUniqueId(), "kills"));
			killer.sendMessage("§7Money: §2" + stats.get(killer.getUniqueId(), "money"));
			killed.sendMessage("§7Exp: §e" + stats.get(killer.getUniqueId(), "exp"));
			killer.sendMessage("§7Streak: §6" + stats.get(killer.getUniqueId(), "streak"));
		}
		
	}
}
