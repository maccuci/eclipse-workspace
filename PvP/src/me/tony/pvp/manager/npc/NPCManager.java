package me.tony.pvp.manager.npc;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NPCManager {

	public void createNPC(Location location, NPCTypes type) {
		Villager villager = (Villager)location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 99999), false);
		switch (type) {
		case QUESTS:
			villager.setAdult();
			villager.setCanPickupItems(false);
			villager.setCustomName(type.getName());
			villager.setCustomNameVisible(true);
			villager.setProfession(Profession.BUTCHER);
			break;
			
		case MARKET_NOCTURE:
			villager.setAdult();
			villager.setCanPickupItems(false);
			villager.setCustomName(type.getName());
			villager.setCustomNameVisible(true);
			villager.setProfession(Profession.BUTCHER);
			break;

		default:
			break;
		}
	}
	
	public static enum NPCTypes {
		QUESTS("§c§lMestre das Quests"), 
		MARKET_NOCTURE("§8§lLíder do Mercado Nortuno");
		
		private String name;
		
		private NPCTypes(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}
