package com.fuzion.core.bukkit.util;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;

import com.fuzion.core.bukkit.BukkitMain;
import com.fuzion.core.master.base.HandlerCommand;

public class ClassLoader {
	
	public boolean load() {
		System.out.println("Starting trying to load all the classes of commands and listeners of the plugin.");

		for (Class<?> classes : ClassGetter.getClassesForPackage(BukkitMain.class, "com.fuzion.core.bukkit")) {
			try {
				if (HandlerCommand.class.isAssignableFrom(classes) && classes != HandlerCommand.class) {
					HandlerCommand command = (HandlerCommand) classes.newInstance();
					if (command.enabled) {
						try{
						    Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
						    commandMapField.setAccessible(true);
						    CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
						    commandMap.register(command.getName(), command);
						} catch (NoSuchFieldException  | IllegalArgumentException | IllegalAccessException exception){
						    exception.printStackTrace();
						}
					}
					System.out.println("The command " + command.getName() + "(" + command.getDescription() + ") its "
							+ (command.enabled ? "enabled and loaded correcly" : "disabled and not loaded") + "!");
				}
			} catch (Exception exception) {
				System.out.println("Error to load the command " + classes.getSimpleName() + ", stopping the process!");
				return false;
			}
			try {
				if (Listener.class.isAssignableFrom(classes)) {
					Listener listener = (Listener) classes.newInstance();
					Bukkit.getPluginManager().registerEvents(listener, BukkitMain.getPlugin());
					System.out.println("The listener " + listener.getClass().getSimpleName() + " was loaded correcly!");
				}
			} catch (Exception exception) {
				System.out.println("Error to load the listener " + classes.getSimpleName() + ", stopping the process!");
				return false;
			}
		}
		return true;
	}

}
