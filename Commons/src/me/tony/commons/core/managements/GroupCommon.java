package me.tony.commons.core.managements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.logging.Level;

import me.tony.commons.core.Commons;
import me.tony.commons.core.account.Group;
import me.tony.commons.core.account.consts.GroupConst;

public class GroupCommon {
	
	private static final HashMap<String, GroupConst> groups = new HashMap<>();
	
	public boolean loadGroups() {
		try {
			PreparedStatement preparedStatement = Commons.getMySQL().getConnection().prepareStatement("SELECT * FROM `global_groups`;");
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				
				if(!groups.containsKey(name)) {
					GroupConst group = new GroupConst(id, name);
					if(group.getId() > 11) {
						group.setVip(true);
					}
					groups.put(name, group);
					Commons.getCommonLogger().log(Level.INFO, "The groups " + name + " was added to the list of groups.");
				}
			}
			resultSet.close();
			preparedStatement.close();
			
			Commons.getCommonLogger().log(Level.INFO, " All ranks was loaded by the tables in mysql.");
		} catch (Exception e) {
			Commons.getCommonLogger().log(Level.SEVERE, "Error when the plugin tryed to load the ranks.", e);
			return false;
		}
		return loadPermissions();
	}
	
	public boolean loadPermissions() {
		Commons.getCommonLogger().log(Level.INFO, "Trying to load all the groups permissions in the table.");
		
		try {
			PreparedStatement preparedStatement = Commons.getMySQL().getConnection().prepareStatement("SELECT * FROM `global_permissions` ORDER BY `group` DESC;");
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int groupid = resultSet.getInt(4);
				
				if (groupid <= 0)
					continue;
				
				GroupConst group = getGroupConst(groupid);
				
				if(group == null)
					continue;
				
				String permission = resultSet.getString(2);
				boolean active = resultSet.getBoolean(5);
				
				Commons.getCommonLogger().log(Level.INFO, "The permission " + permission + "(" + active + ") was added to the group "
						+ group.getName() + ".");

				group.getPermissions().put(permission, active);
			}
			resultSet.close();
			preparedStatement.close();

			Commons.getCommonLogger().log(Level.INFO, "All the permissions of the ranks was loaded.");

		} catch (Exception e) {
			Commons.getCommonLogger().log(Level.SEVERE, "Error when the plugin tryed to load the ranks permissions.", e);
			return false;
		}
		for(Group group : Group.values()) {
			if(!groups.containsKey(group.getName())) {
				createGroup(group.getName(), group.getId());
			}
		}
		return true;
	}
	
	public boolean createGroup(String name, int id) {
		try {
			PreparedStatement insertStatment = Commons.getMySQL().getConnection().prepareStatement("INSERT INTO `global_groups`(`id`, `name`) VALUES (?, ?);");
			insertStatment.setInt(1, id);
			insertStatment.setString(2, name);
			insertStatment.execute();
			insertStatment.close();
			
			if (!groups.containsKey(name)) {
				groups.put(name, new GroupConst(id, name));
			}
			
			Commons.getCommonLogger().log(Level.INFO, "The group " + name + " was create.");
			return true;
		} catch (Exception e) {
			Commons.getCommonLogger().log(Level.SEVERE, "Error when the plugin tried to create the group " + name + ".", e);
			return false;
		}
	}
	
	public Group getGroup(int id) {
		for (Group group : Group.values())
			if (group.getId() == id)
				return group;
		return null;
	}
	
	public GroupConst getGroupConst(int id) {
		for (GroupConst group : groups.values())
			if (group.getId() == id)
				return group;
		return null;
	}
}
