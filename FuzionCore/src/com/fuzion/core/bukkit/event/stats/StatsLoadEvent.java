package com.fuzion.core.bukkit.event.stats;

import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.event.PlayerCancellableEvent;
import com.fuzion.core.master.account.management.StatsManager;

import lombok.Getter;

@Getter
public class StatsLoadEvent extends PlayerCancellableEvent {
	
	private StatsManager statsManager;
	private LoadStats loadStats;

	public StatsLoadEvent(Player player, StatsManager statsManager, LoadStats loadStats) {
		super(player);
		this.statsManager = statsManager;
		this.loadStats = loadStats;
	}

}
