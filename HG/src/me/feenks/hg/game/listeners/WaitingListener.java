package me.feenks.hg.game.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.feenks.hg.api.item.ItemBuilder;
import me.feenks.hg.game.stage.GameStage;
import me.feenks.hg.manager.ScoreboardManager;
import me.feenks.hg.utils.event.SchedulerEvent;
import me.feenks.hg.utils.event.SchedulerEvent.SchedulerType;

public class WaitingListener implements Listener {
	
	private boolean isPreGame() {
		return GameStage.isPregame(GameStage.WAITING);
	}
	
	ScoreboardManager scoreboardManager = new ScoreboardManager();
	
	@EventHandler
	public void joinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ItemBuilder builder = new ItemBuilder();
		
		if(isPreGame()) {
			event.setJoinMessage(null);
			builder.cleanUp(player);
			player.getInventory().addItem(builder.type(Material.CHEST).name("§e§lEscolha seu kit").build());
			scoreboardManager.createScoreboard(player);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		Scoreboard board = e.getPlayer().getScoreboard();
		if (board != null) {
			for (Team t : board.getTeams()) {
				t.unregister();
				t = null;
			}
			for (Objective ob : board.getObjectives()) {
				ob.unregister();
				ob = null;
			}
			board = null;
		}
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	private int i = 0;

	@EventHandler
	public void onUpdate(SchedulerEvent event) {
		if (event.getType() != SchedulerType.TICK)
			return;
		++i;
		if (i % 7 == 0) {
			i = 0;
			scoreboardManager.updateTitleText();
			for (Player p : Bukkit.getOnlinePlayers()) {
				scoreboardManager.updateTitle(p);
			}
		}
	}
}
