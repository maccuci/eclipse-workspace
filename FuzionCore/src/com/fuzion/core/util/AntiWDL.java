package com.fuzion.core.util;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;

public class AntiWDL implements Listener, PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if ((channel.equals("WDL|INIT")) && (!new GroupManager().hasGroupPermission(player.getUniqueId(), Group.GERENTE))) {
			player.kickPlayer("§cVocê não pode usar o World Downloader neste servidor.");
		}
	}
}
