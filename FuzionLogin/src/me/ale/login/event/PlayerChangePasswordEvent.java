package me.ale.login.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerChangePasswordEvent extends PlayerEvent {

	private static final HandlerList HANDLERS = new HandlerList();
	private String oldPassword, newPassword;

	public PlayerChangePasswordEvent(Player player, String oldPassword, String newPassword) {
		super(player);
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return this.oldPassword;
	}

	public String getNewPassword() {
		return this.newPassword;
	}

	@Override
	public HandlerList getHandlers() {
		return PlayerChangePasswordEvent.HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return PlayerChangePasswordEvent.HANDLERS;
	}

}
