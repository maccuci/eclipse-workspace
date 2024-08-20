package com.fuzion.kitpvp.manager.combatlog;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class CombatLogManager {
	
	private HashMap<UUID, CombatLog> combats;

	public CombatLogManager() {
		combats = new HashMap<>();
	}

	public CombatLog getCombatLog(UUID uuid) {
		return combats.containsKey(uuid) ? combats.get(uuid) : null;
	}
	
	public boolean isOnCombated(UUID uuid) {
		return combats.containsKey(uuid);
	}

	public void newCombatLog(UUID damaged, UUID damager) {
		CombatLog damagedCombatLog = combats.get(damaged);
		if (damagedCombatLog == null) {
			damagedCombatLog = combats.put(damaged, new CombatLog(damager));
		} else {
			damagedCombatLog.hitted(damager);
		}
		CombatLog damagerCombatLog = combats.get(damager);
		if (damagerCombatLog == null) {
			damagerCombatLog = combats.put(damager, new CombatLog(damaged));
		} else {
			damagerCombatLog.hitted(damaged);
		}
	}

	public void removeCombatLog(UUID uuid) {
		CombatLog combatLog = combats.get(uuid);
		if (combatLog == null)
			return;
		UUID otherPlayer = combatLog.getCombatLogged();
		CombatLog otherPlayerCombatLog = combats.get(otherPlayer);
		if (otherPlayerCombatLog != null) {
			if (otherPlayerCombatLog.getCombatLogged() == uuid)
				combats.remove(otherPlayer);
		}
		combats.remove(uuid);
	}
	
	public void combatMessage(Player player) {
		player.sendMessage("§cVocê entrou em combate.");
	}
}
