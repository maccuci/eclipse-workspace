package br.com.houldmc.rankup.player.tag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import br.com.houldmc.rankup.player.group.GroupManager;

public class TagManager {
	
	public static char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	public void updateTag(Player player) {
		GroupManager groupManager = new GroupManager();
		
		for(Player players : Bukkit.getOnlinePlayers()) {
			if(Bukkit.getOnlinePlayers().size() == 0)
				return;
			Scoreboard scoreboard = players.getScoreboard();
			if(scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
				scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			}
			Team team = scoreboard.getTeam(alphabet[groupManager.getGroup(player).ordinal()] + player.getName());
			if(team == null) {
				team = scoreboard.registerNewTeam(alphabet[groupManager.getGroup(player).ordinal()] + player.getName());
			}
			team.setPrefix(groupManager.getGroup(player).getTag());
			team.addEntry(player.getName());
			players.setScoreboard(scoreboard);
			player.setScoreboard(scoreboard);
		}
	}
}
