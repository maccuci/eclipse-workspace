package me.ale.pvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.CyonRunnable;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class AutomatonTurretKit extends Kit {
	
	public AutomatonTurretKit() {
		super("AutomatonTurret", Material.DROPPER, new ItemStack(Material.DROPPER), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Crie uma estrutura que atira automaticamente", "§7bolas de neves explosivas.");	
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (!hasKit(player))
			return;
		Action a = event.getAction();
		ItemStack item = player.getItemInHand();
		if (!a.name().contains("RIGHT") && !a.name().contains("LEFT"))
			return;
		if (item == null)
			return;
		ItemStack ITEM = getItem();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		if (a.name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.hasCooldown(player, getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}
		event.setCancelled(true);
		Minecart minecart = (Minecart)player.getWorld().spawnEntity(player.getLocation(), EntityType.MINECART);
		minecart.setPassenger(player);
		minecart.setMaxSpeed(0);
		minecart.setSlowWhenEmpty(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				minecart.remove();
			}
		}, 1000);
		dispare(player);
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 45L));
	}
	
	public static void dispare(Player player) {
		for(int i = 0; i < 100; i++) {
			new CyonRunnable() {
				public void run() {
					if(player.getItemInHand().getType() != Material.DROPPER) {
						cancel();
						return;
					} else {
						Snowball s = player.launchProjectile(Snowball.class);
						Vector v = player.getLocation().getDirection().normalize().multiply(10);
						s.setVelocity(v);
						s.setMetadata("turret", new FixedMetadataValue(Main.getPlugin(), true));
						s.getLocation().getWorld().playEffect(s.getLocation(), Effect.EXPLOSION, 100);
						s.getLocation().getWorld().createExplosion(s.getLocation().getX(), s.getLocation().getY() + 1.0D, s.getLocation().getZ(), 2.1F, false, false);
					}
				}
			}.runTaskTimer(Main.getPlugin(), 0, i);
		}
	}
	
	@EventHandler
	public void onDamageLaser(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) e.getDamager();
			if (s.getShooter() instanceof Player) {
				if (s.hasMetadata("turret")) {
					Player damaged = (Player)e.getEntity();
					damaged.damage(3.5);
				}
			}
		}
	}
}
