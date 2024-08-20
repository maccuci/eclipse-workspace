package me.tony.commons.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import me.tony.commons.api.getter.ClassGetter;
import me.tony.commons.bukkit.BukkitMain;
import me.tony.commons.bukkit.manager.CoreManager;
import me.tony.commons.bukkit.manager.management.Management;

public class ClassLoader extends Management {
	
	public ClassLoader(CoreManager coreManager) {
		super(coreManager, "ClassLoader");
	}
	
	public boolean initialize() {
		return true;
	}

	public void load() {
		if (!initialize()) {
			System.out.println("The plugin wasn't loaded. Please retry later...");
			return;
		}

		System.out.println("Starting trying to load all the classes of commands and listeners of the plugin.");
		for (Class<?> classes : ClassGetter.getClassesForPackage(BukkitMain.class, "me.tony.commons")) {
			try {
				if(TestMethod.class.isAssignableFrom(classes)) {
					TestMethod testMethod = (TestMethod)classes.newInstance();
					System.out.println("This a " + testMethod.getClass().getSimpleName() + " TestMethod!");
				}
				if(Listener.class.isAssignableFrom(classes)) {
					Listener listener = (Listener) classes.newInstance();
					Bukkit.getPluginManager().registerEvents(listener, getCoreManager().getPlugin());
					System.out.println("The listener " + listener.getClass().getSimpleName() + " was loaded correcly!");
				}
			} catch (Exception e) {

			}
		}
	}
}
