package me.feenks.core.bukkit.utils;

public class StringFormat {
	
	public static String correctPluralForm(int value) {
		return value > 1 ? "s" : "";
	}
}
