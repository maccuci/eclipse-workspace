package me.ale.commons.api.cooldown.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import me.ale.commons.api.cooldown.types.Cooldown;


public class CooldownFinshEvent extends CooldownEvent {

    public CooldownFinshEvent(Player player, Cooldown cooldown) {
        super(player, cooldown);
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
