package com.fuzion.kitpvp.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.fuzion.kitpvp.manager.kit.Kit;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;

public class KangarooKit extends Kit {
	
	private ArrayList<Player> kangaroodj = new ArrayList<>();
	
	public KangarooKit() {
		super("Kangaroo", new ItemStack(Material.FIREWORK), Group.BETA, "�7Clique em seu foguete para ganhar um boost para cima se apertado shift ganhe o mesmo boost s� que para cima.");
		addItem(new ItemStack(Material.FIREWORK));
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		Action a = event.getAction();
		ItemStack item = p.getItemInHand();
		if (!a.name().contains("RIGHT") && !a.name().contains("LEFT"))
			return;
		if (!hasKit(p))
			return;
		if (item == null)
			return;
		ItemStack KANGAROO_ITEM = getItem();
		if (!ItemBuilder.isEquals(item, KANGAROO_ITEM))
			return;
		if (a.name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(KANGAROO_ITEM.getDurability());
		p.updateInventory();
		if (CooldownAPI.isInCooldown(p.getUniqueId(), getName())) {
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			p.sendMessage("�cVoc� est� em cooldown durante �c" + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(p.getUniqueId(), getName())));
			return;
		}
		double VECTOR_MULTIPLY = 2;
		if (p.isOnGround()) {
			if (!p.isSneaking()) {
				Vector vector = p.getEyeLocation().getDirection();
				vector.multiply(0.3F * ((VECTOR_MULTIPLY) / 2));
				vector.setY(0.9F * ((VECTOR_MULTIPLY) / 2));
				p.setVelocity(vector);
				if (kangaroodj.contains(p)) {
					kangaroodj.remove(p);
				}
			} else {
				Vector vector = p.getEyeLocation().getDirection();
				vector.multiply(0.3F * ((VECTOR_MULTIPLY) / 2));
				vector.setY(0.55F * ((VECTOR_MULTIPLY) / 2));
				p.setVelocity(vector);
				if (kangaroodj.contains(p)) {
					kangaroodj.remove(p);
				}
			}
		} else {
			if (!kangaroodj.contains(p)) {
				if (!p.isSneaking()) {
					Vector vector = p.getEyeLocation().getDirection();
					vector.multiply(0.3F * ((VECTOR_MULTIPLY) / 2));
					vector.setY(0.85F * ((VECTOR_MULTIPLY) / 2));
					p.setVelocity(vector);
					kangaroodj.add(p);
				} else {
					Vector vector = p.getEyeLocation().getDirection();
					vector.multiply(1.3F * ((VECTOR_MULTIPLY) / 2));
					vector.setY(0.55F * ((VECTOR_MULTIPLY) / 2));
					p.setVelocity(vector);
					kangaroodj.add(p);
				}
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (kangaroodj.contains(event.getPlayer()))
			kangaroodj.remove(event.getPlayer());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (!hasKit(p))
			return;
		if (!kangaroodj.contains(p))
			return;
		if (!p.isOnGround())
			return;
		kangaroodj.remove(p);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		if (!(event.getEntity() instanceof Player))
			return;
		Player kangaroo = (Player) event.getEntity();
		if (!hasKit(kangaroo))
			return;
		CooldownAPI cooldown = new CooldownAPI(kangaroo.getUniqueId(), getName(), 5);
		cooldown.start();
	}
}
