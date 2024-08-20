package com.fuzion.hg.manager.timer;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.fuzion.core.api.date.DateUtils;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.ScoreboardManager;
import com.fuzion.hg.manager.feast.FeastManager;
import com.fuzion.hg.structure.MinifeastStructure;

import lombok.Getter;

public class GameTimer implements TimerScheduler {
	
	@Getter
	public static int count = 0, timer = 0;
	public static FeastManager feastManager = new FeastManager();
	private static MinifeastStructure minifeast = new MinifeastStructure();
	private static Random r = new Random();
	private static int nextMinifeast = 240 + r.nextInt(300);
	public static int FEAST_SPAWN = (17 * 60) + 30;
	public static int BONUSFEAST_SPAWN = 30 * 60;
	
	@Override
	public void pulse() {
		count = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (timer > 0) {
				timer++;
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					new ScoreboardManager().game(p);
				}
				
				if(!feastManager.isChestSpawned() && timer >= FEAST_SPAWN - feastManager.getCounter()) {
					if(feastManager.getCounter() > 0) {
						if(!feastManager.isSpawned()) {
							feastManager.spawnFeast();
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
							}
							new ScoreboardManager().addFeastTimer(feastManager.getFeastLocation(), feastManager.getCounter());
						} else {}
						if ((feastManager.getCounter() % 60 == 0 || (feastManager.getCounter() < 60 && (feastManager.getCounter() % 15 == 0 || feastManager.getCounter() == 10 || feastManager.getCounter() <= 5))))
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.sendMessage("§c§lFEAST §cirá nascer em " + DateUtils.formatDifference(feastManager.getCounter()) + " nas coordernadas X: " + feastManager.getFeastLocation().getBlockX() + ", Y: " + feastManager.getFeastLocation().getBlockY() + ", Z: " + feastManager.getFeastLocation().getBlockZ());
							}
					} else {
						feastManager.spawnChests();
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.sendMessage("§c§lFEAST §cfoi criado nas coordernadas X:" + feastManager.getFeastLocation().getBlockX() + ", Y: " + feastManager.getFeastLocation().getBlockY() + ", Z: " + feastManager.getFeastLocation().getBlockZ());
						}
					}
					feastManager.count();
				}
				if (nextMinifeast <= 0) {
					nextMinifeast = 240 + r.nextInt(300);
					Location place = minifeast.findPlace();
					minifeast.place(place);
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage("§e§lMINIFEAST §efoi criado nas coordernadas X: " + place.getBlockX() + ", Y: " + place.getBlockY() + ", Z: " + place.getBlockZ());
					}
				} else {
					--nextMinifeast;
				}
				
				if(timer == BONUSFEAST_SPAWN) {
					feastManager.spawnBonusFeast();
					Bukkit.broadcastMessage("§5§lBONUSFEAST §5foi criado em algum lugar! Procure por ele.");
					}
				}
			}
		}, 0, 20);
	}
	
	@Override
	public void stop() {
		Bukkit.getScheduler().cancelTask(count);
	}
}
