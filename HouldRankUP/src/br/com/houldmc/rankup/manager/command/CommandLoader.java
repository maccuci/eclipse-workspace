package br.com.houldmc.rankup.manager.command;

import java.io.File;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.api.getter.ClassGetter;

public class CommandLoader {
	
	    private CommandFramework framework = new CommandFramework(Main.getPlugin());

	    public int loadCommandsFromPackage(String packageName) {
	        int i = 0;
	        for (Class<?> commandClass : ClassGetter.getClassesForPackage(framework.getClass(), packageName)) {
	            if (CommandClass.class.isAssignableFrom(commandClass)) {
	                try {
	                    CommandClass commands = (CommandClass) commandClass.newInstance();
	                    framework.registerCommands(commands);
	                    Main.getPlugin().getLogger().warning("The class command " + commandClass.getSimpleName() + " was loaded.");
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    Main.getPlugin().getLogger()
	                            .warning("Not load command class " + commandClass.getSimpleName() + "!");
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
	                    Main.getPlugin().getLogger()
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
