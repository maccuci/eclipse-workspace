package me.tony.commons.bukkit.commands;

import java.io.File;

import me.tony.commons.bukkit.BukkitMain;
import me.tony.commons.core.util.ClassGetter;

public class CommandLoader {
	
	    private static CommandFramework framework = new CommandFramework(BukkitMain.getPlugin());

	    public static int loadCommandsFromPackage(String packageName) {
	        int i = 0;
	        for (Class<?> commandClass : ClassGetter.getClassesForPackage(framework.getClass(), packageName)) {
	            if (CommandClass.class.isAssignableFrom(commandClass)) {
	                try {
	                    CommandClass commands = (CommandClass) commandClass.newInstance();
	                    framework.registerCommands(commands);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    BukkitMain.getPlugin().getLogger()
	                            .warning("Erro ao carregar comandos da classe " + commandClass.getSimpleName() + "!");
	                }
	                i++;
	            }
	        }
	        return i;
	    }

	    public int loadCommandsFromPackage(File jarFile, String packageName) {
	        int i = 0;
	        for (Class<?> commandClass : ClassGetter.getClassesForPackageByFile(jarFile, packageName)) {
	            if (CommandClass.class.isAssignableFrom(commandClass)) {
	                try {
	                    CommandClass commands = (CommandClass) commandClass.newInstance();
	                    framework.registerCommands(commands);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    BukkitMain.getPlugin().getLogger()
	                            .warning("Erro ao carregar comandos da classe " + commandClass.getSimpleName() + "!");
	                }
	                i++;
	            }
	        }
	        return i;
	    }
	
	public static interface CommandClass {
		
	}

}
