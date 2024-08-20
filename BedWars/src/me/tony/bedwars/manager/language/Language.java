package me.tony.bedwars.manager.language;

import java.util.Arrays;

public enum Language {
	
	PORTUGUESE("Português", 1),
	ENGLISH("Inglês", 2);
	
	private String toString;
	private int id;
	
	private Language(String toString, int id) {
		this.toString = toString;
		this.id = id;
	}
	
	public String toString() {
		return toString;
	}
	
	public int getId() {
		return id;
	}
	
	public static Language getLanguage(int lang) {
		return Arrays.asList(values()).stream().filter(lan -> lan.getId() == lang).findFirst().orElse(null);
	}

	public static Language getLanguage(String lang) {
		return Arrays.asList(values()).stream().filter(lan -> lan.name().equalsIgnoreCase(lang)).findFirst().orElse(null);
	}
}
