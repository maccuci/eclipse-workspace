package me.ale.pvp.manager.command;

import me.ale.commons.core.command.CommandLoader.CommandClass;
import me.ale.commons.util.ClassGetter;
import me.ale.pvp.Main;

public class CommandLoader {
	
	private BukkitCommandFramework framework = new BukkitCommandFramework(Main.getPlugin());

    public int loadCommandsFromPackage(String packageName) {
        int i = 0;
        for (Class<?> commandClass : ClassGetter.getClassesForPackage(framework.getClass(), packageName)) {
            if (CommandClass.class.isAssignableFrom(commandClass)) {
                try {
                    CommandClass commands = (CommandClass) commandClass.newInstance();
                    framework.registerCommands(commands);
                } catch (Exception e) {
                    e.printStackTrace();
                    Main.getPlugin().getLogger().warning("Erro ao carregar comandos da classe " + commandClass.getSimpleName() + "!");
                }
                i++;
            }
        }
        return i;
    }

}
