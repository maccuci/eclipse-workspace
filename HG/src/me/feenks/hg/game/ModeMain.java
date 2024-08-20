package me.feenks.hg.game;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import lombok.Getter;
import me.feenks.hg.HardcoreGames;
import me.feenks.hg.api.menu.MenuListener;
import me.feenks.hg.game.listeners.WaitingListener;
import me.feenks.hg.game.stage.GameStage;
import me.feenks.hg.manager.ScoreboardManager;

public class ModeMain {
	
	public static int MINIMUM_PLAYERS = 5;
	public static int INVINCIBILITY_TIME = 120;
	public static int FEAST_SPAWN = (17 * 60) + 30;
	public static int BONUSFEAST_SPAWN = 30 * 60;
	public static int FINALBATTLE_TIME = 45 * 60;
	public static boolean FINISHED;
	
	@Getter
	private HardcoreGames hardcoreGames;
	
	public ModeMain(HardcoreGames hardcoreGames) {
		this.hardcoreGames = hardcoreGames;
	}
	
	public void onLoad() {
		getHardcoreGames().setStage(GameStage.WAITING);
		getHardcoreGames().setMinimumPlayers(MINIMUM_PLAYERS);
	}
	
	public void onEnable() {
		registerListener();
		new ScoreboardManager().scroller.run();
		getHardcoreGames().setStage(GameStage.WAITING);
		getHardcoreGames().getServer().getScheduler().scheduleSyncDelayedTask(hardcoreGames, new Runnable() {
			
			@Override
			public void run() {
				World world = getHardcoreGames().getServer().getWorld("world");
				world.setSpawnLocation(0, getHardcoreGames().getServer().getWorlds().get(0).getHighestBlockYAt(0, 0), 0);
				for (int x = -5; x <= 5; x++)
					for (int z = -5; z <= 5; z++)
						world.getSpawnLocation().clone().add(x * 16, 0, z * 16).getChunk().load();
				world.setDifficulty(Difficulty.NORMAL);
				if (world.hasStorm())
					world.setStorm(false);
				world.setWeatherDuration(999999999);
				world.setGameRuleValue("doDaylightCycle", "false");
				org.bukkit.WorldBorder border = world.getWorldBorder();
				border.setCenter(0, 0);
				border.setSize(1000);
				border.setDamageAmount(4);
				for (Entity e : world.getEntities()) {
					e.remove();
				}
			}
		});
	}
	
	public void startGame() {
		Bukkit.broadcastMessage("§7A partida teve seu inicio!\n§aDesejamos para você uma boa sorte!");
		getHardcoreGames().setTimer(INVINCIBILITY_TIME);
		getHardcoreGames().setStage(GameStage.INVINCIBILITY);
		getHardcoreGames().setTotalPlayers(getHardcoreGames().playersLeft());
	}
	
	public void registerListener() {
		getHardcoreGames().getServer().getPluginManager().registerEvents(new MenuListener(), getHardcoreGames());
		getHardcoreGames().getServer().getPluginManager().registerEvents(new WaitingListener(), getHardcoreGames());
	}
}
