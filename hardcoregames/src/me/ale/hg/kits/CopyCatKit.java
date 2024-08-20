package me.ale.hg.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.ale.hg.manager.kit.Kit;
import me.ale.hg.manager.kit.KitManager;

public class CopyCatKit extends Kit {
	
	public static List<Player> copycat = new ArrayList<>();
	
	public CopyCatKit() {
		super("Copycat", Material.DOUBLE_PLANT, null, "§7Ao matar um jogador roube o kit dele para você.", 13);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setDeathMessage(null);
		
		if(!(event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player))
			return;
		if(event.getEntity().getType() == EntityType.PLAYER && event.getEntity().getKiller().getType() == EntityType.PLAYER) {
			Player killer = (Player)event.getEntity().getKiller();
			Player killed = (Player)event.getEntity();
			
			if(copycat.contains(killer)) {
				String newKit = new KitManager().getKitName(killed);
				new KitManager().setKit(killer, KitManager.getKit(newKit));
				killer.sendMessage("§aSeu novo kit é §e" + KitManager.getKit(newKit).getName());
			}
			copycat.add(killer);
		}
	}
	
	@EventHandler
	public void onCheck(PlayerDeathEvent event) {
		event.setDeathMessage(null);
		
		if(!(event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player))
			return;
		if(event.getEntity().getType() == EntityType.PLAYER && event.getEntity().getKiller().getType() == EntityType.PLAYER) {
			Player killer = (Player)event.getEntity().getKiller();
			Player killed = (Player)event.getEntity();
			String newKit = new KitManager().getKitName(killed);
			new KitManager().setKit(killer, KitManager.getKit(newKit));
			killer.sendMessage("§aSeu novo kit é §e" + KitManager.getKit(newKit).getName());
			if(!copycat.contains(killer)) {
				copycat.add(killer);
			}
			
		}
	}
}
