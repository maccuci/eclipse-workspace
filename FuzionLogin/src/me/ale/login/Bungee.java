package me.ale.login;

import me.ale.login.listener.bungee.Commons;
import net.md_5.bungee.api.plugin.Plugin;

public class Bungee extends Plugin {
	
	private static Bungee bungee;

	@Override
	public void onEnable() {
		Bungee.bungee = this;

		this.getProxy().getPluginManager().registerListener(this, new Commons());
	}

	public static Bungee getInstance() {
		return Bungee.bungee;
	}

}
