package me.feenks.core;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import lombok.Getter;

public class DohanAPI {
	
	public static final String PLUGIN_REAL_NAME = "dcore-4032dl";
	
	@Getter
	private static final Logger logger = Bukkit.getLogger();
	
	public static void debug(String debugStr) {
			logger.info("[DEBUG] " + debugStr);
	}
	
	public static void log(Level level, String message) {
		log(level, message, null);
	}
	
	public static void log(Level level, String message, Throwable throwable) {
		logger.log(level, message, throwable);
	}
	
	public boolean hasSpecialDate(int month, int day) {
		LocalDate date = LocalDate.now();
		
		return date.getMonthValue() == month || date.getDayOfMonth() == day;
	}

}
