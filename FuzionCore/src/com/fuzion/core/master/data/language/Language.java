package com.fuzion.core.master.data.language;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Language {
	
	PORTUGUESE(0, "Português"),
	ENGLISH(1, "Inglês");
	
	private String name;
	private int id;
	
	private Language(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return "Language=" + name + ", ID=" + id;
	}
	
	public static Language getLanguage(int lang) {
		return Arrays.asList(values()).stream().filter(lan -> lan.getId() == lang).findFirst().orElse(null);
	}

	public static Language getLanguage(String lang) {
		return Arrays.asList(values()).stream().filter(lan -> lan.name().equalsIgnoreCase(lang)).findFirst().orElse(null);
	}
}
