package com.fuzion.hg.kits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.api.bar.BarAPI;
import com.fuzion.core.bukkit.event.SchedulerEvent;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.Kit;

public class MadmanKit extends Kit {
	
	private HashMap<Player, Double> madman = new HashMap<>();
	
	public MadmanKit() {
		super("Madman", "Contra times", new ItemStack(Material.PUMPKIN), Group.BETA, "§7Roube a força de todos em sua volta.");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMadman(SchedulerEvent event) {
		if(!GameManager.isGame())
			return;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (hasKit(player)) {
				ArrayList<Player> nearby = new ArrayList<Player>();
				for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
					if (entity instanceof Player) {
						Player target = (Player)entity;
						if(!AdminAPI.admin.contains(target.getUniqueId()) && !GameManager.isSpector(target)) {
							nearby.add(target);
						}
					}
				}
				if (nearby.size() > 0) {
					for (Player t : nearby) {
						if (!madman.containsKey(t))
							madman.put(t, 0.0);
						else
							madman.put(t, madman.get(t) + 0.6);
						
						if (madman.get(t) <= 300.0) {
							if (madman.get(t) % 10.0 == 0.0) {
								t.playSound(t.getLocation(), Sound.AMBIENCE_CAVE, 1, 1);
								t.sendMessage("§cMadman por perto");
							}
							BarAPI.setMessage(t, "Efeito do madman de " + madman.get(t).intValue() * 2 + "%");
							if (player.isOnline())
								BarAPI.setMessage(player, "Efeito do madman de " + madman.get(t).intValue() * 2 + "%");
						}
					}
				}
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (players != player) {
						if (!nearby.contains(players)) {
							if (madman.containsKey(players)) {
								madman.put(players, madman.get(players) - 2);
								BarAPI.setMessage(players, "Efeito do madman de " + madman.get(players).intValue() * 2 + "%");
								if (madman.get(players) < -1) {
									madman.remove(players);
									if (players.isOnline()) {
										BarAPI.removeBar(players);
									}
									if (player.isOnline()) {
										BarAPI.removeBar(player);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onMadmanDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if ((event.getDamager() instanceof Player)) {
				if (madman.containsKey(p)) {
					event.setDamage(event.getDamage() + (madman.get(p) / 10) - 1);
				}
			}
		}
	}
}
