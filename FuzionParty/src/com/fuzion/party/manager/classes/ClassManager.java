package com.fuzion.party.manager.classes;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.fuzion.core.util.ClassGetter;
import com.fuzion.party.Main;

import lombok.Getter;

public class ClassManager {
	private static final HashMap<UUID, ClassType> playerKit = new HashMap<>();
	@Getter
	private static final HashMap<String, ClassType> kits = new HashMap<>();
	
	public static ClassType getKit(String kitName) {
		if (kits.containsKey(kitName.toLowerCase()))
			return kits.get(kitName.toLowerCase());
		else
			System.out.print("Tried to find class '" + kitName + "' but failed!");
		return null;
	}
	
	public static void initializateKits(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(Main.class, packageName)) {
			if (ClassType.class.isAssignableFrom(abilityClass)) {
				try {
					ClassType abilityListener;
					try {
						abilityListener = (ClassType) abilityClass.getConstructor(Main.class).newInstance(Main.getPlugin());
					} catch (Exception e) {
						abilityListener = (ClassType) abilityClass.newInstance();
					}
					String kitName = abilityListener.getClass().getSimpleName().toLowerCase().replace("classe", "");
					kits.put(kitName, abilityListener);
				} catch (Exception e) {
					System.out.print("Erro ao carregar o kit " + abilityClass.getSimpleName());
				}
				i++;
			}
			if(Listener.class.isAssignableFrom(abilityClass)) {
				try {
					Listener listener;
					try {
						listener = (Listener)abilityClass.getConstructor(Main.class).newInstance(Main.getPlugin());
					} catch (Exception e) {
						listener = (Listener)abilityClass.newInstance();
					}
					Main.getPlugin().getServer().getPluginManager().registerEvents(listener, Main.getPlugin());
				} catch (Exception e) {
					System.out.print("Erro ao carregar o kit " + abilityClass.getSimpleName());
				}
			}
		}
		Main.getPlugin().getLogger().info(i + " kits carregados!");
	}
	
	public String getClassName(Player player) {
		return playerKit.get(player.getUniqueId()) != null ? (playerKit.containsKey(player.getUniqueId()) ? playerKit.get(player.getUniqueId()).getName() : "Nenhuma") : null;
	}
	
	public boolean hasKit(Player player) {
		return playerKit.get(player.getUniqueId()).getName() != "Nenhuma";
	}
	
	public void removeKit(Player player) {
		playerKit.remove(player.getUniqueId());
	}
	
	public void setKit(Player player, ClassType classe) {
		playerKit.put(player.getUniqueId(), classe);
	}

}
