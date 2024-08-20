package me.tony.commons.bukkit.event;

import org.bukkit.Bukkit;

import me.tony.commons.bukkit.event.SchedulerEvent.SchedulerType;
import me.tony.commons.bukkit.manager.CoreManager;
import me.tony.commons.bukkit.manager.management.Management;

public class UpdateScheduler extends Management implements Runnable {
	
	public UpdateScheduler(CoreManager coreManager) {
		super(coreManager, "UpdateScheduler");
	}
	
	public boolean initialize() {
		return true;
	}
	
	private long currentTick;

	@Override
	public void run() {
		currentTick++;
        Bukkit.getPluginManager().callEvent(new SchedulerEvent(SchedulerType.TICK, currentTick));

        if (currentTick % 20 == 0) {
            Bukkit.getPluginManager().callEvent(new SchedulerEvent(SchedulerType.SECOND, currentTick));
        }

        if (currentTick % 1200 == 0) {
            Bukkit.getPluginManager().callEvent(new SchedulerEvent(SchedulerType.MINUTE, currentTick));
        }
	}
}
