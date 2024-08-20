package me.feenks.core.bukkit.event;

import org.bukkit.Bukkit;

public class UpdateScheduler implements Runnable {
	
	private long lastSecond = Long.MIN_VALUE;
	  private long lastMinute = Long.MIN_VALUE;
	  
	  public void run() {
	    Bukkit.getPluginManager().callEvent(new SchedulerEvent(SchedulerEvent.SchedulerType.TICK));
	    if (this.lastSecond + 1000L <= System.currentTimeMillis())
	    {
	      Bukkit.getPluginManager().callEvent(new SchedulerEvent(SchedulerEvent.SchedulerType.SECOND));
	      this.lastSecond = System.currentTimeMillis();
	    }
	    if (this.lastMinute + 60000L <= System.currentTimeMillis())
	    {
	      Bukkit.getPluginManager().callEvent(new SchedulerEvent(SchedulerEvent.SchedulerType.MINUTE));
	      this.lastMinute = System.currentTimeMillis();
	    }
	  }
}
