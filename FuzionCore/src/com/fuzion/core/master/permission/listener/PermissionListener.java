package com.fuzion.core.master.permission.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.bukkit.BukkitMain;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;

public class PermissionListener implements Listener {
	
	private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();
	
	public PermissionListener() {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (Player player : BukkitMain.getPlugin().getServer().getOnlinePlayers()) {
					updateAttachment(player, new GroupManager().getGroup(player.getUniqueId()));
				}
			}
		}.runTaskLater(BukkitMain.getPlugin(), 10);
	}
	
	private Permission getCreateWrapper(Player player, String name) {
		Permission perm = BukkitMain.getPlugin().getServer().getPluginManager().getPermission(name);
		if (perm == null) {
			perm = new Permission(name, "fuzion Permissao Interna", PermissionDefault.FALSE);
			BukkitMain.getPlugin().getServer().getPluginManager().addPermission(perm);
		}
		return perm;
	}
	
	protected void updateAttachment(Player player, Group group) {
		PermissionAttachment attach = (PermissionAttachment) attachments.get(player.getUniqueId());
		Permission playerPerm = getCreateWrapper(player, player.getUniqueId().toString());
		if (attach == null) {
			attach = player.addAttachment(BukkitMain.getPlugin());
			attachments.put(player.getUniqueId(), attach);
			attach.setPermission(playerPerm, true);
		}
		playerPerm.getChildren().clear();
		for (String perm : group.getPermissions()) {
			if (!playerPerm.getChildren().containsKey(perm)) {
				playerPerm.getChildren().put(perm, true);
			}
		}
		player.recalculatePermissions();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event) {
		final Player player = event.getPlayer();
		updateAttachment(player, new GroupManager().getGroup(player.getUniqueId()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMonitorLogin(PlayerLoginEvent event) {
		if (event.getResult() != Result.ALLOWED) {
			removeAttachment(event.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		removeAttachment(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent event) {
		removeAttachment(event.getPlayer());
	}

	protected void removeAttachment(Player player) {
		PermissionAttachment attach = (PermissionAttachment) this.attachments.remove(player.getUniqueId());
		if (attach != null) {
			attach.remove();
		}
		BukkitMain.getPlugin().getServer().getPluginManager().removePermission(player.getUniqueId().toString());
	}
	
	public void onDisable() {
		for (PermissionAttachment attach : attachments.values()) {
			attach.remove();
		}
		attachments.clear();
	}
}
