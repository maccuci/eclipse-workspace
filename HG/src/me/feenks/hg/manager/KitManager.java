package me.feenks.hg.manager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import lombok.Getter;
import me.feenks.hg.HardcoreGames;
import me.feenks.hg.constructor.Kit;
import me.feenks.hg.utils.Utils.MasterLogger;
import me.feenks.hg.utils.loader.ClassGetter;

public class KitManager {
	
	private static final HashMap<UUID, Kit> playerKit = new HashMap<>();
	@Getter
	private static final HashMap<String, Kit> kits = new HashMap<>();
	
	private MasterLogger logger = new MasterLogger(HardcoreGames.getPlugin().getMasterLogger(), "Kits");
	
	public void setKit(Player player, Kit kit) {
		playerKit.put(player.getUniqueId(), kit);
	}
	
	public String getKitName(Player player) {
		return getKit(player) != null ? getKit(player).getName() : "Nenhum";
	}
	
	public Kit getKit(Player player) {
		return playerKit.containsKey(player.getUniqueId()) ? playerKit.get(player.getUniqueId()) : null;
	}
	
	public static Kit getKit(String kitName) {
		if (kits.containsKey(kitName.toLowerCase()))
			return kits.get(kitName.toLowerCase());
		else
			System.out.print("Tried to find ability '" + kitName + "' but failed!");
		return null;
	}
	
	public void initializateKits(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(HardcoreGames.class, packageName)) {
			if (Kit.class.isAssignableFrom(abilityClass)) {
				try {
					Kit abilityListener;
					try {
						abilityListener = (Kit) abilityClass.getConstructor(HardcoreGames.class).newInstance(HardcoreGames.getPlugin());
					} catch (Exception e) {
						abilityListener = (Kit) abilityClass.newInstance();
					}
					String kitName = abilityListener.getClass().getSimpleName().toLowerCase().replace("kit", "");
					kits.put(kitName, abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Erro ao carregar o kit " + abilityClass.getSimpleName(), e);
				}
				i++;
			}
			if(Listener.class.isAssignableFrom(abilityClass)) {
				try {
					Listener listener;
					try {
						listener = (Listener)abilityClass.getConstructor(HardcoreGames.class).newInstance(HardcoreGames.getPlugin());
					} catch (Exception e) {
						listener = (Listener)abilityClass.newInstance();
					}
					HardcoreGames.getPlugin().getServer().getPluginManager().registerEvents(listener, HardcoreGames.getPlugin());
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Erro ao carregar o kit " + abilityClass.getSimpleName(), e);
				}
			}
		}
		logger.log(i + " kits was loaded!");
	}

}
