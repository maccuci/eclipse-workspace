package com.fuzion.hg.kits;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.kit.Kit;

public class DestinyKit extends Kit {
	
	public DestinyKit() {
		super("Destiny", "Furtividade", new ItemStack(Material.MOSSY_COBBLESTONE), Group.BETA, "§7Solte um raio que ao atingir um jogador, o mesmo leve dano e voe.");
		addItem(new ItemStack(Material.MOSSY_COBBLESTONE));
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
		ItemStack ITEM = getItems().iterator().next().clone();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		if (a.name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.isInCooldown(player.getUniqueId(), getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage("§cVocê está em cooldown durante " + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		
		event.setCancelled(true);
		Location loc = player.getEyeLocation();
		BlockIterator bta = new BlockIterator(loc, 0.0D, 50);
		while (bta.hasNext()) {
			Location bt = bta.next().getLocation();
			player.getWorld().playEffect(bt, Effect.STEP_SOUND, Material.REDSTONE_ORE, 15);
			player.playSound(bt, Sound.GHAST_FIREBALL, 3.0F, 3.0F);
		}
		Snowball s = player.launchProjectile(Snowball.class);
		Vector v = player.getLocation().getDirection().normalize().multiply(10);
		s.setVelocity(v);
		s.setMetadata("destiny", new FixedMetadataValue(Main.getPlugin(), true));
		player.setVelocity(player.getEyeLocation().getDirection().multiply(-0.6F));
		player.sendMessage("§cVocê disparou o seu raio com sucesso.");
		CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 30);
		cooldown.start();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamageLaser(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) event.getDamager();
			if (s.getShooter() instanceof Player) {
				if (s.hasMetadata("destiny")) {
					Player damaged = (Player) event.getEntity();
					damaged.setVelocity(damaged.getVelocity().add(new Vector(0, 3, 0)));
					damaged.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 2));
					damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 2));
					damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3 * 20, 2));
					damaged.damage(2);
					
				}
			}
		}
	}
}
