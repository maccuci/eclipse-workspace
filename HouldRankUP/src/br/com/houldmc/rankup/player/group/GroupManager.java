package br.com.houldmc.rankup.player.group;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;
import br.com.houldmc.rankup.player.group.list.GroupList;
import br.com.houldmc.rankup.player.tag.TagManager;

public class GroupManager {
	
	public boolean hasGroupPermission(Player player, GroupList group) {
		return getGroup(player).ordinal() <= group.ordinal();
	}
	
	public boolean hasGroup(Player player, GroupList group) {
		return getGroup(player).equals(group);
	}
	
	public GroupList getGroup(Player player) {
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		
		return rankUPPlayer.getGroup();
	}
	
	public boolean isTemporary(Player player) {
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		
		return rankUPPlayer.isGroupTemporary();
	}
	
	public void givePlayerGroup(UUID uniqueId, GroupList group) {
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(uniqueId);
		TagManager tagManager = new TagManager();
		Player player = Bukkit.getPlayer(uniqueId);
		
		try {
			Main.getPlugin().getManager().getMySQL().getConnection().createStatement().execute("UPDATE `players` SET group = '" + group.name() + "' WHERE `uniqueId` = '" + uniqueId.toString() + "';");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(hasGroup(player, group)) {
			return;
		}
		
		rankUPPlayer.setGroup(group);
		rankUPPlayer.setGroupTemporary(false);
		rankUPPlayer.setGroupExpire(0L);
		tagManager.updateTag(player);
		
		if(group.ordinal() > GroupList.HOULD.ordinal()) {
			player.sendMessage("§6§lHould§f§lMC §7» Seu pagamento foi detectado, e você recebeu o grupo " + group.getTag() + "§7. Duração de §bPermanente.");
		} else {
			player.sendMessage("§6§lHould§f§lMC §7» Seu grupo foi alterado para " + group.getTag() + "§7. Duração de §bPermanente.");
		}
	}
	
	public void givePlayerGroup(Player player, GroupList group, long time) {
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player.getUniqueId());
		TagManager tagManager = new TagManager();
		
		Main.getPlugin().getManager().getMySQL().execute("UPDATE `players` SET group = '" + group.name() + "' WHERE `uniqueId` = '" + player.getUniqueId().toString() + "';");
		Main.getPlugin().getManager().getMySQL().execute("UPDATE `players` SET group_temporary = '" + 1 + "' WHERE `uniqueId` = '" + player.getUniqueId().toString() + "';");
		Main.getPlugin().getManager().getMySQL().execute("UPDATE `players` SET group_expire = '" + time + "' WHERE `uniqueId` = '" + player.getUniqueId().toString() + "';");
		
		rankUPPlayer.setGroup(group);
		rankUPPlayer.setGroupTemporary(true);
		rankUPPlayer.setGroupExpire(time);
		tagManager.updateTag(player);
		
		if(group.ordinal() > GroupList.HOULD.ordinal()) {
			player.sendMessage("§6§lHould§f§lMC §7» Seu pagamento foi detectado, e você recebeu o grupo " + group.getTag() + "§7. Duração de §b" + ItemBuilder.getMessage(time));
		} else {
			player.sendMessage("§6§lHould§f§lMC §7» Seu grupo foi alterado para " + group.getTag() + "§7. Duração de §b" + ItemBuilder.getMessage(time));
		}
	}
}
