package me.ale.pvp.manager.kit;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import lombok.Getter;
import me.ale.pvp.Main;
import me.ale.pvp.api.classgetter.ClassGetter;

public class KitManager {
	
	private static final HashMap<UUID, Kit> playerKit = new HashMap<>();
	@Getter
	private static final HashMap<String, Kit> kits = new HashMap<>();
	
	public void initializateKits(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(Main.class, packageName)) {
			if (Kit.class.isAssignableFrom(abilityClass)) {
				try {
					Kit abilityListener;
					try {
						abilityListener = (Kit) abilityClass.getConstructor(Main.class).newInstance(Main.getPlugin());
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
						listener = (Listener)abilityClass.getConstructor(Main.class).newInstance(Main.getPlugin());
					} catch (Exception e) {
						listener = (Listener)abilityClass.newInstance();
					}
					Main.getPlugin().getServer().getPluginManager().registerEvents(listener, Main.getPlugin());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar o kit " + abilityClass.getSimpleName());
				}
			}
		}
		Main.getPlugin().getLogger().info(i + " kits carregados!");
	}
	
	public void setKit(UUID uuid, Kit kit) {
		playerKit.put(uuid, kit);
	}
	
	public void removeKit(UUID uuid) {
		if(!playerKit.containsKey(uuid))
			return;
		playerKit.remove(uuid);
	}
	
	public String getKitName(Player player) {
		return getKitName(player.getUniqueId());
	}
	
	public String getKitName(UUID uuid) {
		return getKit(uuid) != null ? getKit(uuid).getName() : "Nenhum";
	}
	
	public Kit getKit(Player player) {
		return getKit(player.getUniqueId());
	}
	
	public Kit getKit(UUID uuid) {
		return playerKit.containsKey(uuid) ? playerKit.get(uuid) : null;
	}

}
