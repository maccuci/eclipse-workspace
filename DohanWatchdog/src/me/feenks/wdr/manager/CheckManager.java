package me.feenks.wdr.manager;

import java.util.HashMap;

import org.bukkit.event.Listener;

import lombok.Getter;
import me.feenks.core.master.utils.ClassGetter;
import me.feenks.wdr.WDR;

public class CheckManager {
	
	@Getter private static final HashMap<String, Check> hacks = new HashMap<>();
	
	public static void initializateHacks(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(WDR.class, packageName)) {
			if (Check.class.isAssignableFrom(abilityClass)) {
				try {
					Check abilityListener;
					try {
						abilityListener = (Check) abilityClass.getConstructor(WDR.class).newInstance(WDR.getPlugin());
					} catch (Exception e) {
						abilityListener = (Check) abilityClass.newInstance();
					}
					String kitName = abilityListener.getClass().getSimpleName().toLowerCase().replace("check", "");
					hacks.put(kitName, abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar o check do hack " + abilityClass.getSimpleName());
				}
				i++;
			}
			if(Listener.class.isAssignableFrom(abilityClass)) {
				try {
					Listener listener;
					try {
						listener = (Listener)abilityClass.getConstructor(WDR.class).newInstance(WDR.getPlugin());
					} catch (Exception e) {
						listener = (Listener)abilityClass.newInstance();
					}
					WDR.getPlugin().getServer().getPluginManager().registerEvents(listener, WDR.getPlugin());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar o check do hack " + abilityClass.getSimpleName());
				}
			}
		}
		WDR.getPlugin().getLogger().info(i + " checks carregados!");
	}
}
