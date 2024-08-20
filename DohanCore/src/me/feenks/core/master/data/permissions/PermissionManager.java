package me.feenks.core.master.data.permissions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import lombok.Getter;
import me.feenks.core.DohanAPI;
import me.feenks.core.bukkit.BukkitMain;
import me.feenks.core.master.data.permissions.group.Group;
import me.feenks.core.master.data.permissions.group.GroupType;

public class PermissionManager {
	
	@Getter private static final Map<String, Group> groups = new HashMap<>();
	
	public boolean loadGroups() {
		long start = System.currentTimeMillis();
		DohanAPI.debug("Trying to load all the groups in the table.");
		
		try {
			PreparedStatement preparedStatement = BukkitMain.getPlugin().getData().getMysql().prepareStatment("SELECT * FROM `global_groups`;");
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				boolean defaultGroup = false;
				
				if (!groups.containsKey(name)) {
					Group group = new Group(name.toLowerCase(), id, 0, defaultGroup);
					if (group.getId() > 14) {
						group.setVip(true);
					}
					groups.put(name, group);
				}
			}
			
			resultSet.close();
			preparedStatement.close();
			
			DohanAPI.getLogger().log(Level.WARNING, "[" + (System.currentTimeMillis() - start) + "ms] All groups was loaded by the tables in mysql.");
		} catch (Exception e) {
			DohanAPI.log(Level.SEVERE, "Error when the plugin tryed to load the ranks.", e);
			return false;
		}
		return loadPermissions();
	}
	
	public int createGroup(String groupName, int lastId) {
		try {
			long start = System.currentTimeMillis();
			
			PreparedStatement preparedStatement = BukkitMain.getPlugin().getData().getMysql().prepareStatment("SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='" + "core" + "' AND TABLE_NAME=?;");
			preparedStatement.setString(1, "global_groups");
			ResultSet resultSet = preparedStatement.executeQuery();

			resultSet.close();
			preparedStatement.close();
			DohanAPI.debug("[" + (System.currentTimeMillis() - start) + "ms] The last id of table global_groups. Creating the group with last id (" + lastId + ") and with name '" + groupName + "'.");
			
			PreparedStatement insertStatment = BukkitMain.getPlugin().getData().getMysql().prepareStatment("INSERT INTO `global_groups` (`id`, `group`, `permissions`, `members`) VALUES (?, ?, ?, ?);");
			insertStatment.setInt(1, lastId);
			insertStatment.setString(2, groupName);
			insertStatment.setString(3, "");
			insertStatment.setString(4, "");
			insertStatment.execute();
			insertStatment.close();

			return lastId;
		} catch (Exception e) {
			DohanAPI.log(Level.INFO, "Error when the plugin tried to create the group " + groupName + ".", e);
			return -1;
		}
	}
	
	public boolean loadPermissions() {
		DohanAPI.debug("Trying to load all the groups permissions in the table.");
		long start = System.currentTimeMillis();
		
		try {
			PreparedStatement preparedStatement = BukkitMain.getPlugin().getData().getMysql().prepareStatment("SELECT * FROM `global_groups`;");
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				int groupid = resultSet.getInt(1);

				if (groupid <= 0)
					continue;
				
				Group group = getGroup(groupid);
				
				if(group == null)
					continue;
				
				String permission = resultSet.getString(3);
				boolean active = getBooleanByInteger(resultSet.getInt(6));
				
				DohanAPI.debug("The permission " + permission + "(" + active + ") was added to the group "
						+ group.getName() + ".");

				group.getPermissions().put(permission, active);
				
				resultSet.close();
				preparedStatement.close();

				DohanAPI.log(Level.INFO,
						"[" + (System.currentTimeMillis() - start) + "ms] All the permissions of the ranks was loaded.");
			}
		} catch (Exception e) {
			DohanAPI.log(Level.INFO, "Error when the plugin tryed to load the ranks permissions.", e);
			return false;
		}
		for (GroupType groupType : GroupType.values()) {
			if (!groups.containsKey(groupType.getName())) {
				createGroup(groupType.getName(), groupType.getId());
			}
		}
		return true;
	}
	
	Permission getCreateWrapper(String name) {
		Permission perm = Bukkit.getPluginManager().getPermission(name);
		if (perm == null) {
			perm = new Permission(name, "Internal Permission", PermissionDefault.FALSE);
			Bukkit.getPluginManager().addPermission(perm);
		}
		return perm;
	}
	
	public static boolean getBooleanByInteger(Integer id) {
		if (id == 0) {
			return false;
		}
		return true;

	}
	
	public Group getGroup(String name) {
		for(Group group : groups.values())
			if(group.getName() == name)
				return group;
		return null;
	}
	
	public Group getGroup(int id) {
		for (Group group : groups.values())
			if (group.getId() == id)
				return group;
		return null;
	}

}
