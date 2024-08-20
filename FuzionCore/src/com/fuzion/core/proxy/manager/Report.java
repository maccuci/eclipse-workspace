package com.fuzion.core.proxy.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Getter
public class Report {
	
	private ProxiedPlayer reporter, reported;
	private String reason, date;
	private int id;
	
	public Report(ProxiedPlayer reporter, ProxiedPlayer reported, String reason) {
		this.reporter = reporter;
		this.reported = reported;
		this.reason = reason;
		this.date = new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
	@SuppressWarnings("deprecation")
	public void send(ProxiedPlayer staff) {
			staff.sendMessage("§a§lNOVO REPORT!");
			staff.sendMessage(" ");
			staff.sendMessage("Reporter: §b" + reporter.getName());
			staff.sendMessage("Reportado: §b" + reported.getName());
			staff.sendMessage("Motivo: §b" + reason);
			staff.sendMessage(" ");
			staff.sendMessage("§aServidor: §f" + reporter.getServer().getInfo().getName());
	}
}
