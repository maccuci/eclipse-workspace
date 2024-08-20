package br.com.houldmc.rankup.player.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.api.enchantment.enchantments.RemoveBedrockCustomEnchantment;
import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.event.rank.PlayerRankUPEvent;
import br.com.houldmc.rankup.manager.Manager;
import br.com.houldmc.rankup.manager.rank.list.RankList;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;
import br.com.houldmc.rankup.player.tag.TagManager;

public class PlayerHandlerListeners implements Listener {
	
	@EventHandler
	void playerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Manager manager = Main.getPlugin().getManager();
		
		event.setJoinMessage(null);
		player.sendMessage("§6§lHould§f§lMC §7» §fSeja bem-vindo ao nosso servidor de §eRankUP! §fDê o seu melhor para se tornar o §bTOP 1 §fdo servidor! §fDesejamos uma §aboa sorte.\n\n§cVocê recebeu o §ePrestígio I! §cDigite /prestigio para saber mais.");
		new TagManager().updateTag(player);
		player.getInventory().addItem(new ItemBuilder().type(Material.DIAMOND_PICKAXE).glow().enchantment(new RemoveBedrockCustomEnchantment()).lore("§7Bedrock Remover I").build());
		player.teleport(player.getWorld().getSpawnLocation());
		manager.getScoreboardManager().createScoreboard(player);
	}
	
	@EventHandler
	void asyncChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player.getUniqueId());
		
		for(Entity entity : player.getNearbyEntities(15, 15, 15)) {
			if(entity instanceof Player) {
				Player players = (Player)entity;
				
				players.sendMessage("§e[l] §7[" + rankUPPlayer.getRank().getTag() + "§7] " + rankUPPlayer.getGroup().getTag() + player.getName() + " §6§l: §7" + event.getMessage().replace("%", "%%"));
			}
		}
		player.sendMessage("§e[l] §7[" + rankUPPlayer.getRank().getTag() + "§7] " + rankUPPlayer.getGroup().getTag() + player.getName() + " §6§l: §7" + event.getMessage().replace("%", "%%"));
		player.sendMessage("§eNão há ningúem para te escutar.");
		event.setCancelled(true);
	}
	
	@EventHandler
	void playerRankUP(PlayerRankUPEvent event) {
		if(event.getNewRank() == RankList.EMPEROR) {
			event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
			event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
			event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
			Bukkit.broadcastMessage("§6§lHould§f§lMC §7» §fO jogador §e" + event.getPlayer().getName() + " §fchegou no último rank!");
		} else if(event.getPlayer() != null && event.getNewRank().ordinal() < event.getOldRank().ordinal()) {
			Bukkit.broadcastMessage("§6§lHould§f§lMC §7» §fO jogador §e" + event.getPlayer().getName() + " §facabou de evoluir o seu rank!");
		}
		event.getPlayer().sendMessage("§6§lHould§f§lMC §7» §fParabéns, você acabou de evoluir o seu rank para " + event.getNewRank().getTag() + "§f.");
	}

	@EventHandler
	void test(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		for(Entity entity : player.getNearbyEntities(5, 5, 5)) {
			if(entity instanceof Player) {
				Player target = (Player)entity;
				
				if(player.getEyeLocation() == target.getLocation()) {
					player.sendMessage("§aAAAA");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	void scoreboardQuit(PlayerQuitEvent e) {
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
	void scoreboardJoin(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
}
