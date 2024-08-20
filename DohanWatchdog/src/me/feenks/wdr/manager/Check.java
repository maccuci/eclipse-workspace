package me.feenks.wdr.manager;

import org.bukkit.event.Listener;

import lombok.Getter;

public abstract class Check implements Listener {
	
	@Getter private String[] hacks;
	
	public Check(String... hacks) {
		this.hacks = hacks;
	}
	

}
