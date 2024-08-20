package br.com.houldmc.rankup.api.cooldown.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import br.com.houldmc.rankup.api.cooldown.types.Cooldown;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class CooldownEvent extends Event {

    @Getter
    @NonNull
    private Player player;

    @Getter
    @NonNull
    private Cooldown cooldown;
}
