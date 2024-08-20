package me.feenks.core.bukkit.injector.packet.in;

import java.lang.reflect.Field;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;

import me.feenks.core.DohanAPI;
import me.feenks.core.bukkit.BukkitMain;
import me.feenks.core.bukkit.commands.base.BukkitCommand;
import me.feenks.core.bukkit.injector.packet.PacketHandler;
import me.feenks.core.master.utils.ClassGetter;

public class PacketSystemLoader extends PacketHandler {
	
	public PacketSystemLoader() {
		super("SystemLoader");
	}
	
	public boolean process() {
		 for (Class<?> classes : ClassGetter.getClassesForPackage(BukkitCommand.class, "me.feenks.core.bukkit")) {
			 try {
				 if(BukkitCommand.class.isAssignableFrom(classes) && classes != BukkitCommand.class) {
					 BukkitCommand bukkitCommand = (BukkitCommand) classes.newInstance();
					 if(bukkitCommand.enable) {
						try {
							Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
							commandMapField.setAccessible(true);
						    CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
						    commandMap.register(bukkitCommand.getName(), bukkitCommand);
							
						} catch (NoSuchFieldException  | IllegalArgumentException | IllegalAccessException exception) {
							exception.printStackTrace();
						}
					 }
					 DohanAPI.debug("The command " + bukkitCommand.getName() + "(" + bukkitCommand.getDescription() + ") its "
								+ (bukkitCommand.enable ? "enabled and loaded correcly" : "disabled and not loaded") + "!");
				 }
			} catch (Exception exception) {
				DohanAPI.log(Level.INFO, "Error to load the command " + classes.getSimpleName() + ", stopping the process!",
						exception);
				return false;
			}
			 try {
					if (Listener.class.isAssignableFrom(classes) && classes != Listener.class) {
						Listener listener = (Listener) classes.newInstance();
						Bukkit.getPluginManager().registerEvents(listener, BukkitMain.getPlugin());
						DohanAPI.debug("The listener " + listener.getClass().getSimpleName() + " was loaded correcly!");
					}
				} catch (Exception exception) {
					DohanAPI.log(Level.INFO, "Error to load the listener " + classes.getSimpleName() + ", stopping the process!",
							exception);
					return false;
				}
			}
			return true;
		 }
}
