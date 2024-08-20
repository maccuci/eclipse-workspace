package me.ale.pvp.kits;

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

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class KangarooKit extends Kit {
	
	private ArrayList<Player> kangaroodj = new ArrayList<>();
	
	public KangarooKit() {
		super("Kangaroo", Material.FIREWORK, new ItemStack(Material.FIREWORK), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Clique em seu foguete para ganhar um boost para cima", "§7se apertado shift ganhe o mesmo boost", "§7só que para cima.");
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
		if (CooldownAPI.hasCooldown(p, getName())) {
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			p.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(p).getRemaining()));
			return;
		}
		double VECTOR_MULTIPLY = 6.5;
		if (p.isOnGround()) {
			if (!p.isSneaking()) {
				Vector vector = p.getEyeLocation().getDirection();
				vector.multiply(0.3F * ((VECTOR_MULTIPLY) / 10));
				vector.setY(0.9F * ((VECTOR_MULTIPLY) / 10));
				p.setVelocity(vector);
				if (kangaroodj.contains(p)) {
					kangaroodj.remove(p);
				}
			} else {
				Vector vector = p.getEyeLocation().getDirection();
				vector.multiply(0.3F * ((VECTOR_MULTIPLY) / 10));
				vector.setY(0.55F * ((VECTOR_MULTIPLY) / 10));
				p.setVelocity(vector);
				if (kangaroodj.contains(p)) {
					kangaroodj.remove(p);
				}
			}
		} else {
			if (!kangaroodj.contains(p)) {
				if (!p.isSneaking()) {
					Vector vector = p.getEyeLocation().getDirection();
					vector.multiply(0.3F * ((VECTOR_MULTIPLY) / 10));
					vector.setY(0.85F * ((VECTOR_MULTIPLY) / 10));
					p.setVelocity(vector);
					kangaroodj.add(p);
				} else {
					Vector vector = p.getEyeLocation().getDirection();
					vector.multiply(1.3F * ((VECTOR_MULTIPLY) / 10));
					vector.setY(0.55F * ((VECTOR_MULTIPLY) / 10));
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
		if (CooldownAPI.getCooldown(kangaroo).getDuration() <= 0)
			return;
		CooldownAPI.addCooldown(kangaroo, new Cooldown(getName(), 7L));
	}
}
