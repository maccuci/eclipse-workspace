package me.tony.bedwars.manager.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Color;

import lombok.Getter;
import me.tony.bedwars.manager.team.list.TeamList;

public class TeamManager {
	
	@Getter private HashMap<String, Team> teams = new HashMap<>();
	@Getter private List<Team> teamList = new ArrayList<>();
	
	public void insertDefaultTeams() {
		for(TeamList t : TeamList.values()) {
			Team team = new Team(t.getName(), t.getName(), t.getColor());
			teams.put(team.getName(), team);
			teamList.add(team);
		}
		System.out.print("The defaults teams was inserted.");
	}
	
	public Team createTeam(String name, String tag, Color color) {
		Team team = new Team(name, tag, color);
		
		teams.put(name, team);
		System.out.print("Custom team " + name + " has been created.");
		
		return team;
	}
	
	public Team getTeamByName(String name) {
		Team team = null;
		for(Team teams : teams.values()) {
			if(teams.getName() == name)
				team = teams;
		}
		return team;
	}
}
