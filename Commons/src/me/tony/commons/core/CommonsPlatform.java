package me.tony.commons.core;

import java.util.UUID;


public interface CommonsPlatform {
	
	UUID getUUID(String name);
	
	<T> T getPlayerExact(String name, Class<T> clazz);
	
	default Object getPlayerExact(String name) {
		return getPlayerExact(name, Object.class);
	}
	
	void runAsync(Runnable runnable);
	
	<T> T setClassInicializate(Class<T> plugin);
}
