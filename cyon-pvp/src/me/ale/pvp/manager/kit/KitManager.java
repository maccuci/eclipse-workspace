package me.ale.pvp.manager.kit;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import lombok.Getter;
import me.ale.commons.CyonAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.util.ClassGetter;
import me.ale.pvp.Main;

public class KitManager {
	
	private static final HashMap<UUID, Kit> playerKit = new HashMap<>();
	@Getter
	private static final HashMap<String, Kit> kits = new HashMap<>();
	static final ItemBuilder builder = new ItemBuilder();
	
	public static void initializateKits(String packageName) {
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
	
	public static Kit getKit(String kitName) {
		if (kits.containsKey(kitName.toLowerCase()))
			return kits.get(kitName.toLowerCase());
		else
			System.out.print("Tried to find ability '" + kitName + "' but failed!");
		return null;
	}
	
	public void sendKit(Player player, Kit kit) {
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		
		if(kit.getName() == "PvP") {
			player.getInventory().setItem(0, builder.type(Material.STONE_SWORD).name("§3Espada").enchantment(Enchantment.DAMAGE_ALL, 1).build());
		} else {
			player.getInventory().setItem(0, builder.type(Material.STONE_SWORD).name("§3Espada").build());
		}
		
		if(kit.getItem() != null) {
			player.getInventory().addItem(builder.type(kit.getItem().getType()).name(kit.getRarity().getRarityColor() + kit.getName()).build());
		}
		
		for(int i = 0; i < 36; i++) {
			player.getInventory().setItem(i, builder.type(Material.MUSHROOM_SOUP).build());
		}
	}
	
	public void setKit(UUID uuid, Kit kit) {
		playerKit.put(uuid, kit);
		System.out.println(CyonAPI.SERVER_PREFIX + "Player " + Bukkit.getPlayer(uuid).getName() + " has been update your kit.");
	}
	
	public String getKitName(UUID uuid) {
		return getKit(uuid) != null ? getKit(uuid).getName() : "Nenhum";
	}
	
	public Kit getKit(UUID uuid) {
		return playerKit.containsKey(uuid) ? playerKit.get(uuid) : null;
	}

}
