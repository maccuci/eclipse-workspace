package me.ale.pvp.event;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.ale.commons.bukkit.event.PlayerCancellableEvent;
import me.ale.pvp.manager.kit.Kit;

@Getter
public class PlayerBuyKitEvent extends PlayerCancellableEvent {
	
	private Kit kit;
	private Integer price;

	public PlayerBuyKitEvent(Player player, Kit kit, Integer price) {
		super(player);
		this.kit = kit;
		this.price = price;
	}

}
