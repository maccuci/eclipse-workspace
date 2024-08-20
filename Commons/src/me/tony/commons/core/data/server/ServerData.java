package me.tony.commons.core.data.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.tony.commons.core.Commons;
import me.tony.commons.core.data.objects.Server;

public class ServerData {
	
	private final List<Server> servers = new ArrayList<>();
	
	public void createServer(int id, String name, int players, boolean online) {
		createServer(id, name, " ", players, online);
	}
	
	public void createServer(int id, String name, String motd, int players, boolean online) {
		try {
			PreparedStatement insertStatement = Commons.getMySQL().getConnection().prepareStatement("INSERT INTO `servers`(`id`, `name`, `motd`, `players`, `online`) VALUES (?, ?, ?, ?, ?);");
			
			insertStatement.setInt(1, id);
			insertStatement.setString(2, name);
			insertStatement.setString(3, motd);
			insertStatement.setInt(4, players);
			insertStatement.setBoolean(5, online);
			insertStatement.execute();
			insertStatement.close();
			
			servers.add(new Server(id, name, players, online));
			
			Commons.getCommonLogger().log(Level.INFO, "The server " + name + " with id " + id + " was created.");
		} catch (Exception e) {
			Commons.getCommonLogger().log(Level.INFO, e, "Error to created server " + name + ".");
		}
	}
	
	public void kill(Server server) {
		server.kill();
		Commons.getCommonLogger().log(Level.INFO, "The server " + server.getName() + " was stoped.");
	}
	
	public Server getServer(int id) {
		for(Server server : servers) {
			if(server.getId() == id)
				return server;
		}
		return null;
	}
	
	public Server getServer(String name) {
		for(Server server : servers) {
			if(server.getName() == name)
				return server;
		}
		return null;
	}
}
