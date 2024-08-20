package me.ale.pvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class PrisonerKit extends Kit {
	
	public PrisonerKit() {
		super("Prisoner", Material.BEDROCK, new ItemStack(Material.BEDROCK), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Prenda seu inimigo em uma jaula indestrutível", "§7durante 5 segundos.");	
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
		Location loc = player.getEyeLocation();
		BlockIterator bta = new BlockIterator(loc, 0.0D, 50);
		while (bta.hasNext()) {
			Location bt = bta.next().getLocation();
			player.getWorld().playEffect(bt, Effect.STEP_SOUND, Material.BEDROCK, 15);
			player.playSound(bt, Sound.WOOD_CLICK, 3.0F, 3.0F);
		}
		Snowball s = player.launchProjectile(Snowball.class);
		Vector v = player.getLocation().getDirection().normalize().multiply(10);
		s.setVelocity(v);
		s.setMetadata("prisoner", new FixedMetadataValue(Main.getPlugin(), true));
		player.setVelocity(player.getEyeLocation().getDirection().multiply(-0.6F));
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 25L));
	}
	
	@EventHandler
	public void onDamageLaser(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) event.getDamager();
			if (s.getShooter() instanceof Player) {
				Player damaged = (Player) event.getEntity();
				if (s.hasMetadata("prisoner")) {
					damaged.getLocation().add(0.0D, 13.0D, 0.0D).getBlock().setType(Material.BEDROCK);
					damaged.getLocation().add(0.0D, 11.0D, 1.0D).getBlock().setType(Material.BEDROCK);
					damaged.getLocation().add(1.0D, 11.0D, 0.0D).getBlock().setType(Material.BEDROCK);
					damaged.getLocation().add(0.0D, 11.0D, -1.0D).getBlock().setType(Material.BEDROCK);
					damaged.getLocation().add(-1.0D, 11.0D, 0.0D).getBlock().setType(Material.BEDROCK);
					damaged.getLocation().add(0.0D, 10.0D, 0.0D).getBlock().setType(Material.BEDROCK);
					damaged.teleport(damaged.getLocation().add(0.0D, 11.0D, -0.05D));
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
						
						@Override
						public void run() {
//							damaged.getLocation().add(0.0D, 13.0D, 0.0D).getBlock().setType(Material.AIR);
							damaged.getLocation().clone().getBlock().setType(Material.AIR);
							damaged.getLocation().add(0.0D, 11.0D, 1.0D).getBlock().setType(Material.AIR);
							damaged.getLocation().add(1.0D, 11.0D, 0.0D).getBlock().setType(Material.AIR);
							damaged.getLocation().add(0.0D, 11.0D, -1.0D).getBlock().setType(Material.AIR);
							damaged.getLocation().add(-1.0D, 11.0D, 0.0D).getBlock().setType(Material.AIR);
							damaged.getLocation().add(0.0D, 10.0D, 0.0D).getBlock().setType(Material.AIR);
						}
					}, 100);
				}
			}
		}
	}
}
