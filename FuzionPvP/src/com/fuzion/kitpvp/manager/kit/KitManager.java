package com.fuzion.kitpvp.manager.kit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.bukkit.BukkitMain;
import com.fuzion.core.master.account.management.PunishManager.Durations;
import com.fuzion.core.util.ClassGetter;
import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.manager.ScoreboardManager;

import lombok.Getter;

public class KitManager {
	
	private static final HashMap<UUID, Kit> playerKit = new HashMap<>();
	@Getter
	private static final HashMap<String, Kit> kits = new HashMap<>();
	public static List<String> kitsSQL = new ArrayList<>();
	
	public static Kit getKit(String kitName) {
		if (kits.containsKey(kitName.toLowerCase()))
			return kits.get(kitName.toLowerCase());
		else
			System.out.print("Tried to find ability '" + kitName + "' but failed!");
		return null;
	}
	
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
	
	public boolean hasKit(Player player) {
		return getKitName(player) != "Nenhum";
	}
	
	public void removeKit(Player player) {
		playerKit.remove(player.getUniqueId());
	}
	
	public void setKit(Player player, String kit) {
		Kit k = getKit(kit);
		setKit(player, k);
	}
	
	public void setLastKit(Player player, String kit) {
		Kit k = getKit(kit);
		setKit(player, k);
	}
	
	public void setKit(Player player, Kit kit) {
		playerKit.put(player.getUniqueId(), kit);
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		for(int i = 0; i < 36; i++) {
			player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
		}
		player.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
		player.getInventory().setItem(15, new ItemStack(Material.BOWL, 64));
		player.getInventory().setItem(16, new ItemStack(Material.BROWN_MUSHROOM, 64));
		if(kit.getName() == "PvP") {
			player.getInventory().setItem(0, new ItemBuilder().name("§bStone Sword").type(Material.STONE_SWORD).enchantment(Enchantment.DAMAGE_ALL).unbreakable().build());
		} else {
			player.getInventory().setItem(0, new ItemBuilder().name("§dStone Sword").type(Material.STONE_SWORD).unbreakable().build());
		}
		player.getInventory().setItem(8, new ItemBuilder().type(Material.COMPASS).name("§aBússola").build());
		giveItems(player, kit);
		player.updateInventory();
		new ScoreboardManager().updateTeam(player, "kit", getKitName(player));
	}
	
	public void giveItems(Player player, Kit kit) {
		if(kit == null)
			return;
		if(kit.getItems() == null)
			return;
		for (ItemStack item : kit.items) {
			player.getInventory().setItem(1, item.clone());
		}
	}
	
	public String getKitName(Player player) {
		return getKit(player) != null ? getKit(player).getName() : "Nenhum";
	}
	
	public Kit getKit(Player player) {
		return playerKit.containsKey(player.getUniqueId()) ? playerKit.get(player.getUniqueId()) : null;
	}
	
	public boolean hasSQLKit(UUID uuid, String kit) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().resultSet("SELECT * FROM `kits` WHERE `uuid` = '" + uuid.toString() + "' AND `kit` = '" + kit + "';");
			if(resultSet.next()) {
				if(resultSet.getString("duration").contains(Durations.PERMANENT.name())) {
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
		try {
			if(!kitsSQL.contains(kit)) {
				kitsSQL.add(kit);
				BukkitMain.getPlugin().getMysqlBackend().getStatement().executeUpdate("INSERT INTO `kits` (`uuid`, `nickname`, `kit`, `duration`, `temporary`, `expire`) VALUES ('"	+ uuid.toString() + "', '" + player.getName() + "', '" + kit + "', '" + duration.name() + "', '" + Integer.valueOf(temporary ? 1 : 0) + "', '" + expire + "');");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void giveSQLKit(UUID uuid, OfflinePlayer player, String kit, Durations duration, boolean temporary, long expire) {
		if(hasSQLKit(uuid, kit)) {
			return;
		}
		try {
			if (!kitsSQL.contains(kit)) {
				kitsSQL.add(kit);
				BukkitMain.getPlugin().getMysqlBackend().getStatement().executeUpdate("INSERT INTO `kits` (`uuid`, `nickname`, `kit`, `duration`, `temporary`, `expire`) VALUES ('"	+ uuid.toString() + "', '" + player.getName() + "', '" + kit + "', '" + duration.name() + "', '" + Integer.valueOf(temporary ? 1 : 0) + "', '" + expire + "');");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeSQLKit(UUID uuid, String kit) {

		try {
			kitsSQL.remove(kit);
			BukkitMain.getPlugin().getMysqlBackend().getStatement().executeUpdate("DELETE FROM `kits` WHERE `uuid` ='" + uuid.toString() + "' AND `kit` ='" + kit + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getKitSQL(UUID uuid, String kit) {
		if(!hasSQLKit(uuid, kit)) {
			return null;
		}
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().resultSet("SELECT * FROM `kits` WHERE `uuid` = '" + uuid.toString() + "' AND `kit` = '" + kit + "';");
			if(resultSet.next()) {
				return resultSet.getString(kit);
			}
		} catch (SQLException e) {}
		return kit;
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
	
	public String get(UUID uuid, String column) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + uuid + "';");
			if(resultSet.next()) {
				return resultSet.getString(column);
			}
			resultSet.close();
			return null;
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return null;
		}
	}
	
	public void load(Player player) {
		try {
			PreparedStatement statement = BukkitMain.getPlugin().getMysqlBackend().prepareStatment(
					"SELECT * FROM `kits` WHERE (`uuid` ='" + player.getUniqueId().toString() + "');");
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				List<String> kitsFavs = new ArrayList<String>();
				String kits = result.getString("kit");
				if (kits.contains(",")) {
					String[] split;
					for (int length = (split = kits.split(",")).length, i = 0; i < length; ++i) {
						final String str = split[i];
						kitsFavs.add(str);
					}
				} else {
					kitsFavs.add(kits);
				}
				statement.close();
				result.close();
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
