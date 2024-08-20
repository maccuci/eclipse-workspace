package me.tony.commons.core.account.consts;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.tony.commons.core.Commons;
import me.tony.commons.core.account.CommonsPlayer;
import me.tony.commons.core.account.Group;

@Getter
public class TagConst {
	
	private String name;
	private Group group;
	private int id;
	
	public TagConst(String name, Group group, int id) {
		this.name = name;
		this.group = group;
		this.id = id;
	}
	
	public int getOrder() {
		return group.getId();
	}
	
	public boolean hasTag(Player player) {
		if (name.equals("Membro") || name.startsWith("Membro")) {
			return true;
		}

		CommonsPlayer account = Commons.getAccount(player.getUniqueId());

		if (account.getGroup() != null) {
			if (name.equals("Diretor") || name.startsWith("Diretor")) {
				if (account.getGroup().getId() > 4) {
					return false;
				}
			}
			if (name.equals("Developer") || name.startsWith("Developer")) {
				if (account.getGroup().getId() > 3) {
					return false;
				}
			}
			if (name.equals("Dono") || name.startsWith("Dono")) {
				if (account.getGroup().getId() > 2) {
					return false;
				}
			}
		}

		return player.getPlayer().hasPermission("commons.tag." + name)
				|| player.getPlayer().hasPermission("commons.tag." + name);
	}
}
