package com.fuzion.kitpvp.manager;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.fuzion.core.FuzionAPI;
import com.fuzion.core.master.account.management.StatsManager;
import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.command.TagCommand;
import com.fuzion.kitpvp.manager.kit.KitManager;
import com.fuzion.kitpvp.manager.scoreboard.SimpleScoreboard;

public class ScoreboardManager {
	
	public void createScoreboard(Player player) {
		if(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
			SimpleScoreboard scoreboard = new SimpleScoreboard("§6§lFuzion§e§lPvP");
			scoreboard.blankLine();
			scoreboard.add("§fKills: §7");
			scoreboard.add("§fDeaths: §7");
			scoreboard.add("§fKillstreak: §a");
			scoreboard.add("§fXP: §6");
			scoreboard.blankLine();
			scoreboard.add("§fRanking: §b");
			scoreboard.add("§fKit: §e");
			scoreboard.blankLine();
			scoreboard.add("§a" + FuzionAPI.ADDRESS);
			scoreboard.build();
			scoreboard.send(player);
			
			player.getScoreboard().registerNewTeam("kills").addEntry("§fKills: §7");
			player.getScoreboard().registerNewTeam("deaths").addEntry("§fDeaths: §7");
			player.getScoreboard().registerNewTeam("streak").addEntry("§fKillstreak: §a");
			player.getScoreboard().registerNewTeam("xp").addEntry("§fXP: §6");
			player.getScoreboard().registerNewTeam("position").addEntry("§fRanking: §b");
			player.getScoreboard().registerNewTeam("kit").addEntry("§fKit: §e");
		}
		StatsManager statsManager = new StatsManager();
		DecimalFormat format = new DecimalFormat("#.###");
		player.getScoreboard().getTeam("kills").setSuffix("" + statsManager.get(player.getUniqueId(), "kills"));
		player.getScoreboard().getTeam("deaths").setSuffix("" + statsManager.get(player.getUniqueId(), "deaths"));
		player.getScoreboard().getTeam("streak").setSuffix("" + statsManager.get(player.getUniqueId(), "streak"));
		player.getScoreboard().getTeam("position").setSuffix("" + Main.getPlugin().getPositionManager().getPosition(player) + "°");
		player.getScoreboard().getTeam("xp").setSuffix("" + format.format(statsManager.get(player.getUniqueId(), "xp")));
		player.getScoreboard().getTeam("kit").setSuffix(new KitManager().getKitName(player));
	}
	
    public void lookSomeone(Player player, Player target) {
        final Scoreboard sb = player.getScoreboard();
        Objective ob = sb.getObjective("taker");
        if (ob != null) {
            if (ob.getDisplayName().replace("Streak §e| Kit ", "").equalsIgnoreCase(new KitManager().getKitName(target))) {
                return;
            }
            ob.unregister();
        }
        ob = sb.registerNewObjective("taker", "dummy");
        ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
        String kitName = "Streak §e| Kit " + new KitManager().getKitName(target);
        if (kitName.length() > 32) {
            kitName = kitName.substring(0, 32);
        }
        ob.setDisplayName(kitName);
        ob.getScore(target.getName()).setScore(new StatsManager().get(target.getUniqueId(), "streak"));
    }
    
    public void notLookingAtSomeone(Player player) {
        final Scoreboard sb = player.getScoreboard();
        final Objective ob = sb.getObjective("taker");
        if (ob != null) {
            ob.unregister();
        }
        player.setScoreboard(sb);
    }
	
	@SuppressWarnings("deprecation")
	public void updateTeams(Player player) {
		StatsManager statsManager = new StatsManager();
		DecimalFormat format = new DecimalFormat("#.###");
		updateTeam(player, "kills", "" + statsManager.get(player.getUniqueId(), "kills"));
		updateTeam(player, "deaths", "" + statsManager.get(player.getUniqueId(), "deaths"));
		updateTeam(player, "streak", "" + statsManager.get(player.getUniqueId(), "streak"));
		updateTeam(player, "xp", "" +  format.format(statsManager.get(player.getUniqueId(), "xp")));
		updateTeam(player, "position", "" + Main.getPlugin().getPositionManager().getPosition(player) + "°");
		updateTeam(player, "kit", new KitManager().getKitName(player));
		updateTeam(player, "streak", "" + Bukkit.getOnlinePlayers().length);
	}
	
	public void updateTeam(Player player, String teamID, String text) {
		if(TagCommand.scoreboard.contains(player))
			return;
		if(player.getScoreboard().getTeam(teamID) == null)
			return;
		player.getScoreboard().getTeam(teamID).setSuffix(text);
	}
}
