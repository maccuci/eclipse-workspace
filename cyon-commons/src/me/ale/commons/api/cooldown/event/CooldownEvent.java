package me.ale.commons.api.cooldown.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.ale.commons.api.cooldown.types.Cooldown;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;


@RequiredArgsConstructor
public abstract class CooldownEvent extends Event {

    @Getter
    @NonNull
    private Player player;

    @Getter
    @NonNull
    private Cooldown cooldown;
}
