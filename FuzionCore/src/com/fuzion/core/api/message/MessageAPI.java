package com.fuzion.core.api.message;

import org.bukkit.entity.Player;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@Getter
public class MessageAPI {
	
	private TextComponent textComponent;
	private BaseComponent baseComponent;
	
	public MessageAPI(TextComponent textComponent) {
		this.textComponent = textComponent;
	}
	
	public void send(Player player, String text, String hoverMessage) {
		(this.textComponent = new TextComponent(text)).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (BaseComponent[])new TextComponent[] { new TextComponent(hoverMessage) }));
	}
}
