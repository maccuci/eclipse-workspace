package me.tony.he4rt.cosmetics.list;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.Listener;

import lombok.Getter;
import me.tony.he4rt.cosmetics.Main;
import me.tony.he4rt.cosmetics.list.type.GadgetType;
import me.tony.he4rt.cosmetics.util.ClassGetter;

public class CosmeticsManager {
	
	@Getter private static final Map<String, GadgetType> cosmetics = new HashMap<>();
	
	public static void initializateCosmetics() {
		for(Class<?> classes : ClassGetter.getClassesForPackage(Main.class, "me.tony.he4rt.cosmetics.list.cosmetics")) {
			if (GadgetType.class.isAssignableFrom(classes)) {
				try {
					GadgetType abilityListener;
					try {
						abilityListener = (GadgetType) classes.getConstructor(Main.class).newInstance(Main.getPlugin());
					} catch (Exception e) {
						abilityListener = (GadgetType) classes.newInstance();
					}
					String kitName = abilityListener.getName();
					cosmetics.put(kitName, abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(Listener.class.isAssignableFrom(classes)) {
				try {
					Listener listener;
					try {
						listener = (Listener)classes.getConstructor(Main.class).newInstance(Main.getPlugin());
					} catch (Exception e) {
						listener = (Listener)classes.newInstance();
					}
					Main.getPlugin().getServer().getPluginManager().registerEvents(listener, Main.getPlugin());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
