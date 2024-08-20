package com.fuzion.core.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.fuzion.core.api.bar.BarAPI;
import com.fuzion.core.api.hologram2.HologramListener;
import com.fuzion.core.api.menu.MenuListener;
import com.fuzion.core.bukkit.event.UpdateScheduler;
import com.fuzion.core.bukkit.listener.BukkitListener;
import com.fuzion.core.master.CorePlugin;
import com.fuzion.core.master.account.management.PositionManager;
import com.fuzion.core.master.clan.ClanManager;
import com.fuzion.core.master.commands.CommandLoader;
import com.fuzion.core.master.data.DataMaster;
import com.fuzion.core.master.data.mysql.Mysql;
import com.fuzion.core.master.permission.listener.PermissionListener;
import com.fuzion.core.util.AntiWDL;

import lombok.Getter;

public class BukkitMain extends CorePlugin {
	
	public static BukkitMain getPlugin() {
		return BukkitMain.getPlugin(BukkitMain.class);
	}
	
	@Getter
	private Mysql mysqlBackend;
	
	@Getter
	private ClanManager clanManager;
	
	@Getter
	private DataMaster dataMaster;
	
	@Override
	public void run() {
		dataMaster = new DataMaster();
		mysqlBackend = dataMaster.getMysql();
		new PositionManager().run();
		dataMaster.openConnection();
		clanManager = new ClanManager();
		
		getLogger().info("Ativado.");
		
		new CommandLoader().loadCommandsFromPackage("com.fuzion.core.bukkit.command");
		
	    getServer().getMessenger().registerIncomingPluginChannel(this, "WDL|INIT", new AntiWDL());
	    getServer().getMessenger().registerOutgoingPluginChannel(this, "WDL|CONTROL");
	    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

	    getServer().getScheduler().runTaskTimer(this, new UpdateScheduler(), 1L, 1L);
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new BukkitListener(), this);
		pm.registerEvents(new AntiWDL(), this);
		pm.registerEvents(new MenuListener(), this);
		pm.registerEvents(new PermissionListener(), this);
		pm.registerEvents(new BarAPI(this), this);
		pm.registerEvents(new HologramListener(), this);
	    
//	    new BukkitRunnable() {
//			
//			@Override
//			public void run() {
//				List<String> messages = getConfig().getStringList("Broadcast");
//				int random = new Random().nextInt(messages.size());
//				Bukkit.broadcastMessage(messages.get(random).replace("&", "§").replace("%prefix", "§6§lFuzionMC §7»"));
//			}
//		}.runTaskTimer(this, 0, 20 * 80);
//	}
}
	
	@Override
	public void stop() {
		
	}
}
