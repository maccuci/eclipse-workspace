package me.ale.pvp.manager.achievement;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.bukkit.event.league.PlayerLeagueChangeEvent;
import me.ale.commons.core.account.League;
import me.ale.commons.core.account.manager.LeagueManager;
import me.ale.commons.core.account.manager.StatsManager;
import me.ale.pvp.Main;
import me.ale.pvp.manager.achievement.type.AchievementType;

public class AchievementListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Main.getPlugin().getAchievementManager().giveAchievement(player, AchievementType.FIRST_JOIN);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		Main.getPlugin().getAchievementManager().giveAchievement(player, AchievementType.FIRST_CHAT);
	}
	
	@EventHandler
	public void onPlayerLeagueChange(PlayerLeagueChangeEvent event) {
		if (event.getPlayer() != null && event.getNewLeague().ordinal() > event.getOldLeague().ordinal()) {
			if(event.getNewLeague() == League.SENSEI) {
				Main.getPlugin().getAchievementManager().giveAchievement(event.getPlayer(), AchievementType.LEAGUE_SENSEI);
			}
			Main.getPlugin().getAchievementManager().giveAchievement(event.getPlayer(), AchievementType.FIRST_LEAGUE);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(!(event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player))
			return;
		
		if(event.getEntity().getType() == EntityType.PLAYER && event.getEntity().getKiller().getType() == EntityType.PLAYER) {
			Player killer = (Player)event.getEntity().getKiller();
			Player killed = (Player)event.getEntity();
			StatsManager manager = BukkitMain.getPlugin().getStatsManager();
			LeagueManager leagueManager = new LeagueManager(manager);
			
			Main.getPlugin().getAchievementManager().giveAchievement(killer, AchievementType.FIRST_KILL);
			Main.getPlugin().getAchievementManager().giveAchievement(killed, AchievementType.FIRST_DEATH);
			
			if(manager.get(killer.getUniqueId(), "kills") == 10) {
				Main.getPlugin().getAchievementManager().giveAchievement(killer, AchievementType.REACH_10);
			}
			
			if(manager.get(killer.getUniqueId(), "kills") == 100) {
				Main.getPlugin().getAchievementManager().giveAchievement(killer, AchievementType.REACH_100);
			}
			
			if(manager.get(killer.getUniqueId(), "kills") == 1000) {
				Main.getPlugin().getAchievementManager().giveAchievement(killer, AchievementType.REACH_1000);
			}
			
			if(manager.get(killer.getUniqueId(), "streak") == 1) {
				Main.getPlugin().getAchievementManager().giveAchievement(killer, AchievementType.FIRST_STREAK);
			}
			
			if(manager.get(killer.getUniqueId(), "streak") == 5) {
				Main.getPlugin().getAchievementManager().giveAchievement(killer, AchievementType.STREAK_5);
			}
			
			if(leagueManager.getLeague(killed.getUniqueId()) == League.SENSEI) {
				Main.getPlugin().getAchievementManager().giveAchievement(killer, AchievementType.KILL_SENSEI);
			}
		}
	}
}
