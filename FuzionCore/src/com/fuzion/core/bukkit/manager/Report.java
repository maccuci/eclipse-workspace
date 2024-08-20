package com.fuzion.core.bukkit.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.entity.Player;

import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;

import lombok.Getter;

@Getter
public class Report {
	
	private Player reporter, reported;
	private String reason, date;
	private int id;
	
	public Report(Player reporter, Player reported, String reason) {
		this.reporter = reporter;
		this.reported = reported;
		this.reason = reason;
		this.date = new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
	public void send(Player staff) {
		GroupManager groupManager = new GroupManager();
		
		if(groupManager.hasGroupPermission(staff.getUniqueId(), Group.YOUTUBERPLUS)) {
			staff.sendMessage("§a§lNOVO REPORT!");
			staff.sendMessage(" ");
			staff.sendMessage("Reporter: §b" + reporter.getName());
			staff.sendMessage("Reportado: §b" + reported.getName());
			staff.sendMessage("Motivo: §b" + reason);
			staff.sendMessage(" ");
			staff.sendMessage("§aServidor: §fPvP");
		}
	}
}
