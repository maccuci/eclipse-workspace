package com.fuzion.kitpvp.manager.command;

import com.fuzion.core.master.commands.CommandLoader.CommandClass;
import com.fuzion.core.util.ClassGetter;
import com.fuzion.kitpvp.Main;

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
