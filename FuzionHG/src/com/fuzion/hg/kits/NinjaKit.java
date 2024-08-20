package com.fuzion.hg.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class NinjaKit extends Kit {
	
	private HashMap<String, NinjaHit> ninjaHits = new HashMap<>();
	
	public NinjaKit() {
		super("Ninja", "Contra times", new ItemStack(Material.EMERALD), Group.BETA, "§7Teleporte-se até o último jogador que você hitou.");
	}
	
	@EventHandler
	public void onNinjaHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			final Player damager = (Player) event.getDamager();
			Player damaged = (Player) event.getEntity();
			if (hasKit(damager)) {
				NinjaHit ninjaHit = ninjaHits.get(damager.getName());
				if (ninjaHit == null)
					ninjaHit = new NinjaHit(damaged);
				else
					ninjaHit.setTarget(damaged);
				ninjaHits.put(damager.getName(), ninjaHit);
			}
		}
	}

	@EventHandler
	public void onShift(PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();
		if (!event.isSneaking())
			return;
		if (!hasKit(p))
			return;
		if (!ninjaHits.containsKey(p.getName()))
			return;
		NinjaHit ninjaHit = ninjaHits.get(p.getName());
		Player target = ninjaHit.getTarget();
		if (target.isDead())
			return;
		if (ninjaHit.getTargetExpires() < System.currentTimeMillis())
			return;
		if ((p.getLocation().distance(target.getLocation()) > 15) || p.getLocation().getY() - target.getLocation().getY() > 30) {
			p.sendMessage("§cEste jogador está muito longe.");
			return;
		}
		if (CooldownAPI.isInCooldown(p.getUniqueId(), getName())) {
			p.sendMessage("§cVocê está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(p.getUniqueId(), getName())));
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			return;
		}
		p.teleport(target.getLocation());
		p.sendMessage("§aVocê foi teleportado!");
		p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5F, 1.0F);
		CooldownAPI cooldown = new CooldownAPI(p.getUniqueId(), getName(), 7);
		cooldown.start();
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if (!ninjaHits.containsKey(p.getName()))
			return;
		ninjaHits.remove(p.getName());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (!ninjaHits.containsKey(p.getName()))
			return;
		ninjaHits.remove(p.getName());
	}

	private static class NinjaHit {
		private Player target;
		private long targetExpires;

		public NinjaHit(Player target) {
			this.target = target;
			this.targetExpires = System.currentTimeMillis() + 15000;
		}

		public Player getTarget() {
			return target;
		}

		public long getTargetExpires() {
			return targetExpires;
		}

		public void setTarget(Player player) {
			this.target = player;
			this.targetExpires = System.currentTimeMillis() + 20000;
		}

	}
}
