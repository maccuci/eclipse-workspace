package com.fuzion.hg.kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class GrenadierKit extends Kit {
	
	private ArrayList<String> grenadiers = new ArrayList<>();
	
	public GrenadierKit() {
		super("Grenadier", "Dano", new ItemStack(Material.FIREWORK_CHARGE), Group.BETA, "§7Jogue uma granada altamente destrutiva no mapa.");
		addItem(new ItemStack(Material.FIREWORK_CHARGE));
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
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 5));
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage("§cVocê está em cooldown durante " + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		
		event.setCancelled(true);
		Snowball s = (Snowball)player.launchProjectile(Snowball.class);
		s.setVelocity(player.getLocation().getDirection().multiply(1.5D));
		grenadiers.add(s.getUniqueId().toString());
		CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 10);
		cooldown.start();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		if((event.getEntity() instanceof Snowball)) {
			Snowball s = (Snowball)event.getEntity();
			
			if(grenadiers.contains(s.getUniqueId().toString())) {
				Location loc = s.getLocation();
				if ((Bukkit.getPlayer(((Player)s.getShooter()).getName()) != null)) {
					loc.getWorld().createExplosion(loc.getX(), loc.getY() + 1.0D, loc.getZ(), 2.1F, false, false);
					for (int i = 0; i <= 7; i++) {
						loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, i);
					}
				}
			}
		}
	}
}
