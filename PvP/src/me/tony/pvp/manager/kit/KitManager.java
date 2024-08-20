package me.tony.pvp.manager.kit;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.Listener;

import me.tony.commons.api.getter.ClassGetter;
import me.tony.pvp.Main;


public class KitManager {
	
	private static final HashMap<UUID, Kit> playerKit = new HashMap<>();
	private static final HashMap<String, Kit> kits = new HashMap<>();
	
	public static void initializateKits(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(Main.class, packageName)) {
			if (Kit.class.isAssignableFrom(abilityClass)) {
				try {
					Kit abilityListener;
					try {
						abilityListener = (Kit) abilityClass.getConstructor(Main.class).newInstance(Main.getInstance());
					} catch (Exception e) {
						abilityListener = (Kit) abilityClass.newInstance();
					}
					String kitName = abilityListener.getClass().getSimpleName().toLowerCase().replace("kit", "");
					kits.put(kitName, abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar o kit " + abilityClass.getSimpleName());
				}
				i++;
			}
			if(Listener.class.isAssignableFrom(abilityClass)) {
				try {
					Listener listener;
					try {
						listener = (Listener)abilityClass.getConstructor(Main.class).newInstance(Main.getInstance());
					} catch (Exception e) {
						listener = (Listener)abilityClass.newInstance();
					}
					Main.getInstance().getServer().getPluginManager().registerEvents(listener, Main.getInstance());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar o kit " + abilityClass.getSimpleName());
				}
			}
		}
		Main.getInstance().getLogger().info(i + " kits carregados!");
	}
	
	public static Kit getKit(String kitName) {
		if (kits.containsKey(kitName.toLowerCase()))
			return kits.get(kitName.toLowerCase());
		else
			System.out.print("Tried to find ability '" + kitName + "' but failed!");
		return null;
	}
	
	public String getKitName(UUID uuid) {
		return getKit(uuid) != null ? getKit(uuid).getName() : "Nenhum";
	}
	
	public Kit getKit(UUID uuid) {
		return playerKit.containsKey(uuid) ? playerKit.get(uuid) : null;
	}
	
	public static HashMap<String, Kit> getKits() {
		return kits;
	}
}
