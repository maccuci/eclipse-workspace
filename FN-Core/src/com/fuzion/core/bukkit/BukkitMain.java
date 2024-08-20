package com.fuzion.core.bukkit;

import java.text.NumberFormat;

import com.fuzion.core.bukkit.event.UpdateScheduler;
import com.fuzion.core.bukkit.util.ClassLoader;
import com.fuzion.core.master.Core;
import com.fuzion.core.master.data.mysql.Mysql;

import lombok.Getter;

public class BukkitMain extends Core {
	
	public static BukkitMain getPlugin() {
		return BukkitMain.getPlugin(BukkitMain.class);
	}
	
	@Getter
	private Mysql mysql;
	
	@Override
	public void running() {
		mysql = new Mysql();
		mysql.openConnection();
		new UpdateScheduler().run();
		
		new ClassLoader().load();
		
		NumberFormat format = NumberFormat.getInstance();
		System.out.println("[" + format.format(System.currentTimeMillis()) + "ms] Server has been started.");
	}
	
	@Override
	public void stoping() {
		
	}
}
