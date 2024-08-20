package com.vexy.thepit.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.vexy.thepit.Main;
import com.vexy.thepit.player.PitPlayer;
import com.vexy.thepit.player.PitPlayerCRUD;
import com.vexy.thepit.player.PitPlayerManager;
import com.vexy.thepit.player.scoreboard.PlayerScoreboard;
import com.vexy.thepit.util.Formatter;
import com.vexy.thepit.util.Util;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class PlayerListener implements Listener {
	
	PitPlayerManager pitManager = Main.getPlugin().getPitPlayerManager();
	
	@EventHandler
	void preLogin(AsyncPlayerPreLoginEvent event) {
		PitPlayerCRUD pitPlayerCRUD = new PitPlayerCRUD();
		PitPlayer pitPlayer = new PitPlayer();
		
		pitPlayerCRUD.create(pitPlayer);
		
	/*	if(!pitManager.contains(event.getUniqueId())) {
			pitPlayerCRUD.create(pitPlayer);
			pitPlayer.setUuid(event.getUniqueId());
			pitPlayer.setGold(0);
			pitPlayer.setLevel(1);
			pitPlayer.setXp(0);
			pitPlayer.setPrestige(1);
			pitPlayer.setPlScoreboard(new PlayerScoreboard());
			pitManager.add(event.getUniqueId(), pitPlayer);
		} else {
			pitPlayerCRUD.read(pitPlayer.getUuid());
		}*/
		
/*		if(pitPlayerCRUD.read(event.getUniqueId()) != null) {
			pitManager.add(event.getUniqueId(), pitPlayerCRUD.read(event.getUniqueId()));
		}*/
	}
	
	@EventHandler
	void playerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		/*if(!pitManager.contains(player.getUniqueId()))
			player.kickPlayer("§e§lTHEPIT\n§cAs suas informações estão provavelmente corrompidas.\n§cEntre em contato com o desenvolvedor.");*/
		
		PitPlayer pitPlayer = pitManager.retriever(player.getUniqueId());
		
		 player.setLevel(pitPlayer.getLevel());
	     player.setExp(pitPlayer.getXp());
	     
	     pitPlayer.setPlScoreboard(new PlayerScoreboard());
	     
	     new PlayerScoreboard().showScoreboard(player);
	     player.setPlayerListName(Formatter.formatChat(pitPlayer) + " " + player.getName());
	}
	
	@EventHandler
	void chat(AsyncPlayerChatEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		PitPlayer pitPlayer = pitManager.retriever(uuid);
		
		event.setFormat(event.getPlayer().getDisplayName() + " " + Formatter.formatChat(pitPlayer) + "§f: " + event.getMessage().replace("%", "%%"));
	}
	
	@EventHandler
    void playerDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(true);
            }
        }
    }
	
    @EventHandler
    void playerDamageByPlayerEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getDamager() instanceof Player) {
                Player damaged = (Player) event.getDamager();
                Util.sendActionBar(damaged, Util.progressBar(Math.round(player.getHealth()), 20, 20, "♥", "§c", "§7"));
            }
        }
    }
    
    @EventHandler
    void playerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity().getKiller();
        PitPlayer pitPlayer = pitManager.retriever(player.getUniqueId());
        
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				PacketPlayInClientCommand in = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
				EntityPlayer cPlayer = ((CraftPlayer) event.getEntity()).getHandle();
				cPlayer.playerConnection.a(in);
			}
		}, 1);

        player.setExp(+20);

        pitPlayer.setGold(+70);
        pitPlayer.setXp(Math.round(player.getExp()));
        pitPlayer.setLevel(Math.round(player.getLevel()));
        pitPlayer.getPlScoreboard().update(player);
    }
    
    @EventHandler
	void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		
		Scoreboard board = event.getPlayer().getScoreboard();
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
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	void onPlayerJoinListener(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	@EventHandler
	void toggle(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		PitPlayerCRUD pitPlayerCRUD = new PitPlayerCRUD();
		PitPlayer pitPlayer = pitManager.retriever(player.getUniqueId());
		
		if(event.isSneaking()) {
			pitPlayer.setLevel(11);
			player.setLevel(pitPlayer.getLevel());
		    player.setExp(pitPlayer.getXp());
		    pitPlayerCRUD.update(pitPlayer);
			pitPlayer.getPlScoreboard().update(player);
		}
	}
}
