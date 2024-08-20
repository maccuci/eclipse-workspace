package br.com.houldmc.rankup.player.clan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.api.menu.ClickType;
import br.com.houldmc.rankup.api.menu.MenuClickHandler;
import br.com.houldmc.rankup.api.menu.MenuInventory;
import br.com.houldmc.rankup.api.menu.MenuItem;
import br.com.houldmc.rankup.player.clan.utilitaries.Clan;

public class ClanManager {
	
	private static final List<Clan> clans = new ArrayList<>();
	
	public void setup() {
		try {
			ResultSet resultSet = Main.getPlugin().getManager().getMySQL().getConnection().createStatement().executeQuery("SELECT * FROM `clans`");
			
			while(resultSet.next()) {
				String name = resultSet.getString("name");
				UUID owner = UUID.fromString(resultSet.getString("owner"));
				String tag = resultSet.getString("tag");
				String member = resultSet.getString("members");
				int money = resultSet.getInt("money");
				List<String> members = new ArrayList<>();
				for(String m : member.split(", ")) {
					members.add(m);
				}
				clans.add(new Clan(owner, name, tag, members, money));
			}
			System.out.println("The all clans was loaded.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Clan createClan(UUID owner, String name, String tag, String member) {
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `clans` (`owner`, `name`, `tag`, `members`, `money`) VALUES (?, ?, ?, ?, ?);");
			List<String> members = new ArrayList<>();
			
			preparedStatement.setString(1, owner.toString());
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, tag);
			preparedStatement.setString(4, member);
			preparedStatement.setInt(5, 0);
			preparedStatement.execute();
			preparedStatement.close();
			
			members.add(member);
			Clan clan = new Clan(owner, name, tag, members, 0);
			clans.add(clan);
			
			return clan;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void addMemberToClan(String name, String targetName) {
		String names = "";
		try {
			Clan clan = null;
			for(Clan clans : clans) {
				if(clan.getName().equalsIgnoreCase(name)) {
					clan = clans;
				}
			}
			clan.getMembers().add(targetName);
			
			for(String members : clan.getMembers()) {
				names += members + ", ";
			}
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `clans` SET `members` =? WHERE name =?");
			preparedStatement.setString(1, names);
			preparedStatement.setString(2, name);
			preparedStatement.execute();
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeMemberClan(String name, String targetName) {
		String names = "";
		try {
			Clan clan = null;
			for(Clan clans : clans) {
				if(clan.getName().equalsIgnoreCase(name)) {
					clan = clans;
				}
			}
			clan.getMembers().remove(targetName);
			
			for(String members : clan.getMembers()) {
				names += members + ", ";
			}
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `clans` SET `members` =? WHERE name =?");
			preparedStatement.setString(1, names);
			preparedStatement.setString(2, name);
			preparedStatement.execute();
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasClan(String name) {
		boolean b = false;
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `clans` WHERE name=?");
			preparedStatement.setString(1, name);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public boolean hasClanTag(String tag) {
		boolean b = false;
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `clans` WHERE tag=?");
			preparedStatement.setString(1, tag);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public boolean hasPlayerClan(UUID uniqueId) {
		Player player = Bukkit.getPlayer(uniqueId);
		boolean b = false;
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM `clans` WHERE members=?");
			preparedStatement.setString(1, player.getName());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public boolean hasPlayerClanOwner(UUID uniqueId) {
		boolean b = false;
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM `clans` WHERE owner=?");
			preparedStatement.setString(1, uniqueId.toString());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public void addMoney(Player player, String clan, int amount) {
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `clans` SET `money`=`money`+" + amount + " WHERE name ='" + clan +"'");
			preparedStatement.execute();
			preparedStatement.close();
			getClan(player).setMoney(getClan(player).getMoney() + amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeMoney(Player player, String clan, int amount) {
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `clans` SET `money` =? WHERE name =?");
			preparedStatement.setInt(1, getClan(player).getMoney() - amount);
			preparedStatement.setString(2, clan);
			preparedStatement.execute();
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getClanName(Player player) {
		String clanName = "";
		
		for(Clan clan : clans) {
			for(String member : clan.getMembers()) {
				if(member.equalsIgnoreCase(player.getName())) {
					clanName = clan.getName();
				} else {
					clanName = "Nenhum";
				}
			}
		}
		return clanName;
	}
	
	public Clan getClan(Player player) {
		Clan clan = null;
		
		for(Clan clans : clans) {
			for(String member : clans.getMembers()) {
				if(member.equalsIgnoreCase(player.getName())) {
					clan = clans;
				}
			}
		}
		return clan;
	}
	
	public Clan getClanByTag(String tag) {
		Clan clan = null;
		
		for(Clan clans : clans) {
				if(clans.getName().equalsIgnoreCase(tag)) {
					clan = clans;
			}
		}
		return clan;
	}
	
	public Clan getClanByTagg(String tag) {
		try {
			Connection connection = Main.getPlugin().getManager().getMySQL().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `clans` WHERE tag=?");
			preparedStatement.setString(1, tag);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				for(int i = 0; i < clans.size(); i++) {
					Clan clan = clans.get(i);
					
					if(clan.getTag() == tag) {
						return clan;
					}
				}
			}
			preparedStatement.execute();
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public String broadcast(String message) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			for(Clan clan : clans) {
				if (getClanName(player) == clan.getName()) {
					player.sendMessage("§6§lClan §7» §f" + message);
				}
			}
		}
		return message;
	}
	
	public void info(Player player, Clan clan, int page) {
		MenuInventory menu = new MenuInventory("§8Informações do clan " + clan.getName() + "(" + clan.getTag() + ")", 6);
		ItemBuilder itemBuilder = new ItemBuilder();
		List<MenuItem> items = new ArrayList<>();
		int itemsPerPage = 21;
		
		for(int i = 0; i < 9; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		}
		
		for(int i = 45; i < 54; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build());
		}
		
		menu.setItem(13, itemBuilder.type(Material.SIGN).name("§eInformações").lore("§7Moedas: §f" + clan.getMoney(), "§7Membros: §f" + clan.getMembers().size(), "§7Dono: §f" + Bukkit.getPlayer(clan.getOwner()).getName()).build());
		
		for(String name : clan.getMembers()) {
			Player clanMember = Bukkit.getPlayerExact(name);

			items.add(new MenuItem(itemBuilder.type(Material.SKULL_ITEM).name("§e" + clanMember.getName()).durability(3).skin(player.getName()).build()));
		}
		
		int pageStart = 0;
		int pageEnd = itemsPerPage;
		if (page > 1) {
			pageStart = ((page - 1) * itemsPerPage);
			pageEnd = (page * itemsPerPage);
		}
		if (pageEnd > items.size()) {
			pageEnd = items.size();
		}
		if (page == 1) {
			
		} else {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).name("§aPágina Anterior").durability(10).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					info(arg0, clan, page - 1);
				}
			}), 3);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).name("§aPágina Posterior").durability(10).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					info(arg0, clan, page + 1);
				}
			}), 5);
		} else {
			menu.setItem(new ItemBuilder().type(Material.INK_SACK).name("§7Próxima página").durability(8).build(), 35);
		}
		int w = 19;

		for (int i = pageStart; i < pageEnd; i++) {
			MenuItem item = items.get(i);
			menu.setItem(item, w);
			if (w % 9 == 7) {
				w += 3;
				continue;
			}
			w += 1;
		}
		
		menu.open(player);
	}
}
