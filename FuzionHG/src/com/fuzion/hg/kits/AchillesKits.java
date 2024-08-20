package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class AchillesKits extends Kit {
	
	public AchillesKits() {
		super("Achilles", "Estratégia", new ItemStack(Material.WOOD_SWORD), Group.BETA, "§7Tome mais dano de itens de madeira.");
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getEntity();
		Player d = (Player) event.getDamager();
		ItemStack inHand = d.getItemInHand();
		if (!hasKit(p)) {
			return;
		}
		if (inHand.getType().name().contains("WOOD_")) {
			event.setDamage(event.getDamage() + 3.0D);
		} else if (inHand.getType().name().contains("DIAMOND_")) {
			event.setDamage(event.getDamage() - 3.0D);
			d.sendMessage("§cVocê está batendo em um achilles! Achilies tem fraqueza a itens de madeira.");
		} else if (inHand.getType().name().contains("IRON_")) {
			event.setDamage(event.getDamage() - 2.0D);
			d.sendMessage("§cVocê está batendo em um achilles! Achilies tem fraqueza a itens de madeira.");
		} else if (inHand.getType().name().contains("STONE_")) {
			event.setDamage(event.getDamage() - 2.0D);
			d.sendMessage("§cVocê está batendo em um achilles! Achilies tem fraqueza a itens de madeira.");
		}
	}
}
