package com.fuzion.kitpvp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.management.StatsManager;
import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.manager.ScoreboardManager;
import com.fuzion.kitpvp.manager.kit.KitManager;
import com.fuzion.kitpvp.manager.onevsone.Warp1v1;
import com.fuzion.kitpvp.manager.position.PositionManager;
import com.fuzion.kitpvp.manager.warp.WarpManager;
import com.fuzion.kitpvp.manager.warp.Warps;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;

public class DeathListener implements Listener {
	
	int xpReward = 0;
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		
		event.setDeathMessage("");
		Player killed = event.getEntity();
		event.getDrops().clear();
		new KitManager().removeKit(killed);
		new ScoreboardManager().createScoreboard(killed);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				PacketPlayInClientCommand in = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
				EntityPlayer cPlayer = ((CraftPlayer) killed).getHandle();
				cPlayer.playerConnection.a(in);
			}
		}, 1);
		
		if(WarpManager.getWarp(killed).equals(Warps.ONE_VS_ONE)) {
			if(Warp1v1.isIn1v1(killed)) {
				WarpManager.teleport(killed, Warps.ONE_VS_ONE);
				return;
			}
		} else if(WarpManager.getWarp(killed).equals(Warps.FPS)) {
			WarpManager.teleport(killed, Warps.FPS);
			return;
		} else if(WarpManager.getWarp(killed).equals(Warps.LAVA)) {
			WarpManager.teleport(killed, Warps.LAVA);
			return;
		} else if(WarpManager.getWarp(killed).equals(Warps.SPAWN)) {
			WarpManager.teleport(killed, Warps.SPAWN);
			return;
		}
		
		if (event.getEntity().getKiller() instanceof Player) {
			Player killer = killed.getKiller();
			
			if (killer != event.getEntity()) {
				StatsManager statsManager = new StatsManager();
				PositionManager position = new PositionManager();
				
				if(WarpManager.getWarp(killer).equals(Warps.LAVA)) {
					return;
				}
				
				if(position.getPosition(killed) > position.getPosition(killer)) {
					if(statsManager.get(killed.getUniqueId(), "kills") > statsManager.get(killer.getUniqueId(), "kills")) {
						xpReward += 2 * 10 / statsManager.get(killed.getUniqueId(), "kills");
					} else {
						xpReward += 2 * 5 / statsManager.get(killer.getUniqueId(), "kills");
					}
				} else {
					xpReward += 2 * 2 / statsManager.get(killer.getUniqueId(), "kills");
				}
				
				if(Warp1v1.fighting.containsKey(killed) && Warp1v1.fighting.containsKey(killer)) {
					killer.sendMessage("§eVocê matou o jogador " + killed.getName() + " e restou " + ItemBuilder.getAmount(killer, Material.MUSHROOM_SOUP) + " sopas.");
					killed.sendMessage("§eVocê morreu para o jogador " + killer.getName() + " e restou " + ItemBuilder.getAmount(killed, Material.MUSHROOM_SOUP) + " sopas.");
					if(statsManager.get(killed.getUniqueId(), "streak") >= 1) {
						statsManager.set(killed.getUniqueId(), "streak", 0);
					}
					statsManager.set(killed.getUniqueId(), "deaths", statsManager.get(killed.getUniqueId(), "deaths") + 1);
					if(statsManager.get(killed.getUniqueId(), "xp") <= 0) {
						return;
					}
					
					statsManager.set(killed.getUniqueId(), "xp", statsManager.get(killed.getUniqueId(), "xp") - 3);
					killed.sendMessage("§cVocê morreu para o jogador " + killer.getName() + " e perdeu 3XPs.");
					
					//killer
					statsManager.set(killer.getUniqueId(), "streak", statsManager.get(killer.getUniqueId(), "streak") + 1);
					statsManager.set(killer.getUniqueId(), "xp", statsManager.get(killer.getUniqueId(), "xp") + xpReward);
					statsManager.set(killer.getUniqueId(), "kills", statsManager.get(killer.getUniqueId(), "kills") + 1);
					killer.sendMessage("§aVocê matou o jogador " + killer.getName() + " e ganhou " + xpReward + "XPs.");
					
					if(statsManager.get(killer.getUniqueId(), "streak") % 5 == 0) {
						Bukkit.broadcastMessage("§eO Jogador " + killer.getName() + " conseguiu um killstreak de §6" + statsManager.get(killer.getUniqueId(), "streak") + "§e!");
					}
					
					new ScoreboardManager().updateTeams(killer);
					new ScoreboardManager().updateTeams(killed);
					killer.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
					killer.getInventory().setItem(3, new ItemBuilder().type(Material.BLAZE_ROD).name("§eDesafie um Jogador").build());
					killer.getInventory().setItem(4, new ItemBuilder().type(Material.WORKBENCH).name("§cDesafio Customizado").build());
					killer.getInventory().setItem(5, new ItemBuilder().type(Material.INK_SACK).name("§71v1 Rápido").durability(8).build());
				} else {
					if(statsManager.get(killed.getUniqueId(), "streak") >= 1) {
						statsManager.set(killed.getUniqueId(), "streak", 0);
					}
					statsManager.set(killed.getUniqueId(), "deaths", statsManager.get(killed.getUniqueId(), "deaths") + 1);
					if(statsManager.get(killed.getUniqueId(), "xp") <= 0) {
						return;
					}
					
					statsManager.set(killed.getUniqueId(), "xp", statsManager.get(killed.getUniqueId(), "xp") - 3);
					killed.sendMessage("§cVocê morreu para o jogador " + killer.getName() + " e perdeu 3XPs.");
					
					//killer
					statsManager.set(killer.getUniqueId(), "streak", statsManager.get(killer.getUniqueId(), "streak") + 1);
					statsManager.set(killer.getUniqueId(), "xp", statsManager.get(killer.getUniqueId(), "xp") + xpReward);
					statsManager.set(killer.getUniqueId(), "kills", statsManager.get(killer.getUniqueId(), "kills") + 1);
					killer.sendMessage("§aVocê matou o jogador " + killer.getName() + " e ganhou " + xpReward + "XPs.");
					
					if(statsManager.get(killer.getUniqueId(), "streak") % 5 == 0) {
						Bukkit.broadcastMessage("§eO Jogador " + killer.getName() + " conseguiu um killstreak de §6" + statsManager.get(killer.getUniqueId(), "streak") + "§e!");
					}
					
					new ScoreboardManager().updateTeams(killer);
					new ScoreboardManager().updateTeams(killed);
				}
			}
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		player.setFoodLevel(20);
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		new ScoreboardManager().updateTeams(player);
		
		if(WarpManager.getWarp(player).equals(Warps.ONE_VS_ONE)) {
			if (Warp1v1.isIn1v1(player)) {
				WarpManager.teleport(player, Warps.ONE_VS_ONE);
				return;
			}
		} else if(WarpManager.getWarp(player).equals(Warps.FPS)) {
			WarpManager.teleport(player, Warps.FPS);
			return;
		} else if(WarpManager.getWarp(player).equals(Warps.LAVA)) {
			WarpManager.teleport(player, Warps.LAVA);
			return;
		} else if(WarpManager.getWarp(player).equals(Warps.SPAWN)) {
			event.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
			return;
		}
	}
}
