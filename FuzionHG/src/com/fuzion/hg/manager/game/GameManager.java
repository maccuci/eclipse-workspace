package com.fuzion.hg.manager.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.api.title.TitleAPI;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.hg.Main;
import com.fuzion.hg.kits.SurpriseKit;
import com.fuzion.hg.manager.kit.KitManager;
import com.fuzion.hg.manager.kit.KitMatch;
import com.fuzion.hg.manager.timer.GameTimer;
import com.fuzion.hg.manager.timer.InvincibilityTimer;
import com.fuzion.hg.manager.timer.PreGameTimer;

import lombok.Getter;

public class GameManager {
	
	private static List<Player> players = new ArrayList<>();
	private static List<Player> spectors = new ArrayList<>();
	private static HashMap<Player, Integer> kills = new HashMap<>();
	public static boolean FINISHED;
	public static boolean DAMAGE = false;
	
	@Getter
	public static GameStage stage;
	
	public static void addPlayer(Player player) {
		players.add(player);
	}
	
	public static void removePlayer(Player player) {
		players.remove(player);
	}
	
	public static boolean isPlayer(Player player) {
		return players.contains(player);
	}
	
	public static void addSpector(Player player) {
		spectors.add(player);
	}
	
	public static void removeSpector(Player player) {
		spectors.remove(player);
	}
	
	public static void addKill(Player player) {
		if(!kills.containsKey(player)) {
			kills.put(player, 1);
		}
		kills.put(player, kills.get(player) + 1);
	}
	
	public static List<Player> getPlayers() {
		return players;
	}
	
	public static List<Player> getSpectors() {
		return spectors;
	}
	
	public static int getPlayerSize() {
		return players.size() - AdminAPI.admin.size();
	}
	
	public static int getSpectorSize() {
		return spectors.size();
	}
	
	public static int getKills(Player player) {
		return kills.containsKey(player) ? kills.get(player) : 0;
	}
	
	public static void resetKills(Player player) {
		kills.put(player, 0);
		kills.remove(player);
	}
	
	public static boolean isSpector(Player player) {
		return spectors.contains(player) && AdminAPI.admin.contains(player.getUniqueId());
	}
	
	public static void setStage(GameStage newStage) {
		stage = newStage;
	}
	
	public static boolean isPreGame() {
		return stage == GameStage.WAITING;
	}
	
	public static boolean isInvincibility() {
		return stage == GameStage.INVINCIBILITY;
	}
	
	public static boolean isGame() {
		return stage == GameStage.GAME;
	}
	
	@SuppressWarnings("deprecation")
	public static void startGame() {
		new PreGameTimer().stop();
		new InvincibilityTimer().pulse();
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(isSpector(player))
				return;
			if(!player.isOnline())
				return;
			TagManager tagManager = new TagManager(player);
			tagManager.setTag(tagManager.getTag());
			tagManager.update();
			for(Player online : Bukkit.getOnlinePlayers()) {
				new TagManager(online).update();
			}
			player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 3f, 3f);
			player.playSound(player.getLocation(), Sound.WITHER_DEATH, 3f, 3f);
			player.closeInventory();
			player.getInventory().clear();
			player.setGameMode(org.bukkit.GameMode.SURVIVAL);
			surprise(player);
			new KitManager().giveItems(player, new KitManager().getKit(player));
			player.getInventory().addItem(new ItemStack(Material.COMPASS));
			player.sendMessage("§aA partida começou! Boa sorte para todos os participantes da partida.");
			TitleAPI title = new TitleAPI("§aPartida iniciou!", "§fBoa sorte e bom jogo.", 1, 2, 1);
			title.send(player);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void checkWinner() {
		if(!GameManager.isGame())
			return;
		if(getPlayerSize() > 1)
			return;
		Player pWin = null;
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(isSpector(player))
				return;
			if (!player.isOnline())
				continue;
			pWin = player;
			break;
		}
		if (pWin == null) {
			Bukkit.broadcastMessage("§cNão foi possível encontrar o ganhador. Reiniciando.");
			Bukkit.shutdown();
			return;
		}
		FINISHED = true;
		new GameTimer().stop();
		Player winner = pWin;
		for (Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
			resetKills(player);
			resetKills(winner);
			player.sendMessage("§aO Jogador " + winner.getName() + " ganhou a partida com " + getKills(winner) + " kills.");
		}
		
		Main.getPlugin().getWinnerManager().addWinner(winner.getName());
		winner.sendMessage("§aVocê ganhou a tag §2§lWINNER §ae todos os kits!");
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				winner.kickPlayer("§aParabéns! Você ficou em 1º lugar da partida, com isso você foi o ganhador!\n§6Você fez 0 Kills.");
				for (Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
					player.kickPlayer("§aQue pena. Você não foi o ganhador desta partida. Tente na próxima.\n§aO Jogador " + winner.getName() + " ganhou a partida com 0 kills.");
				}
				KitMatch.removeKitMatch(winner);
				Bukkit.shutdown();
			}
		}.runTaskLater(Main.getPlugin(), 20 * 30);
	}
	
	
	public static void surprise(Player player) {
		if(new KitManager().getKitName(player) == "Surprise") {
			String kitName = SurpriseKit.getViableKit();
			new KitManager().setKit(player, KitManager.getKit(kitName));
			player.sendMessage("§aO surprise escolheu o kit " + kitName + " para você!");
		}
	}
	
	public static String translateStageName(GameStage stage) {
		String str = "";
		switch (stage) {
		case WAITING:
			str = "AGUARDANDO";
			break;
			
		case INVINCIBILITY:
			str = "INVENCIBILIDADE";
			break;
			
		case GAME:
			str = "JOGO";
			break;

		default:
			break;
		}
		return str;
	}
}
