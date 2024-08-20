package com.fuzion.hg.manager.kit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.bukkit.BukkitMain;
import com.fuzion.core.master.account.management.PunishManager.Durations;
import com.fuzion.core.util.ClassGetter;
import com.fuzion.hg.Main;

import lombok.Getter;

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
	
	public void setKit(Player player, Kit kit) {
		playerKit.put(player.getUniqueId(), kit);
	}
	
	public static Kit getKit(String kitName) {
		if (kits.containsKey(kitName.toLowerCase()))
			return kits.get(kitName.toLowerCase());
		else
			System.out.print("Tried to find ability '" + kitName + "' but failed!");
		return null;
	}
	
	public String getKitName(Player player) {
		return getKit(player) != null ? getKit(player).getName() : "Nenhum";
	}

	public Kit getKit(Player player) {
		return playerKit.containsKey(player.getUniqueId()) ? playerKit.get(player.getUniqueId()) : null;
	}
	
	public void giveItems(Player player, Kit kit) {
		if(kit == null)
			return;
		if(kit.getItems() == null)
			return;
		for (ItemStack item : kit.items) {
			player.getInventory().addItem(item.clone());
		}
	}
	
	public boolean hasSQLKit(UUID uuid, String kit) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().resultSet("SELECT * FROM `kits` WHERE `uuid` = '" + uuid.toString() + "' AND `kit` = '" + kit + "';");
			if(resultSet.next()) {
				if(resultSet.getString("duration").equals("PERMANENT")) {
					return resultSet.getString("uuid") != null;
				} else {
					long expire = resultSet.getLong("expire");
					int seconds = (int)((expire - System.currentTimeMillis()) / 1000L);
					return resultSet.getString("uuid") != null && seconds > 0;
				}
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void giveSQLKit(UUID uuid, Player player, String kit, Durations duration, boolean temporary, long expire) {
		if(hasSQLKit(uuid, kit)) {
			return;
		}
		BukkitMain.getPlugin().getMysqlBackend().execute("INSERT INTO `kits` (`uuid`, `nickname`, `kit`, `duration`, `temporary`, `expire`) VALUES ('" + uuid.toString() + "', '" + player.getName() + "', '" + kit + "', '" + duration.name() + "', '" + Integer.valueOf(temporary ? 1 : 0) + "', '" + expire + "');");
	}
	
	public void giveSQLKit(UUID uuid, OfflinePlayer player, String kit, Durations duration, boolean temporary, long expire) {
		if(hasSQLKit(uuid, kit)) {
			return;
		}
		BukkitMain.getPlugin().getMysqlBackend().execute("INSERT INTO `kits` (`uuid`, `nickname`, `kit`, `duration`, `temporary`, `expire`) VALUES ('" + uuid.toString() + "', '" + player.getName() + "', '" + kit + "', '" + duration.name() + "', '" + Integer.valueOf(temporary ? 1 : 0) + "', '" + expire + "');");
	}
	
	public void removeSQLKit(UUID uuid, String kit) {
		if(!hasSQLKit(uuid, kit)) {
			return;
		}
		BukkitMain.getPlugin().getMysqlBackend().execute("DELETE FROM `kits` WHERE `uuid` = '" + uuid.toString() + "' AND `kit` = '" + kit + "';");
	}
	
	public boolean isTemporary(UUID uuid) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `kits` WHERE `uuid` = '" + uuid + "';");
			if(resultSet.next()) {
				return resultSet.getBoolean("temporary");
			}
			resultSet.close();
			return false;
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return true;
		}
	}
}
