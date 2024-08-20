package me.feenks.pvp.events;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.feenks.core.bukkit.event.PlayerCancellableEvent;
import me.feenks.pvp.manager.ability.Ability;

public class PlayerSelectAbilityEvent extends PlayerCancellableEvent {

	@Getter
	private Ability ability;
	
	public PlayerSelectAbilityEvent(Player player, Ability ability) {
		super(player);
		this.ability = ability;
	}

}
