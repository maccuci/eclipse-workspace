package me.ale.pvp.kits;

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

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class GrenadierKit extends Kit {
	
	private ArrayList<String> grenadiers = new ArrayList<>();
	
	public GrenadierKit() {
		super("Grenadier", Material.FIREWORK_CHARGE, new ItemStack(Material.FIREWORK_CHARGE), KitRarity.MYSTIC, Rank.SIMPLE, "§7Jogue uma granada altamente destrutiva no mapa.");	
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
		Snowball s = (Snowball)player.launchProjectile(Snowball.class);
		s.setVelocity(player.getLocation().getDirection().multiply(1.5D));
		grenadiers.add(s.getUniqueId().toString());
		 CooldownAPI.addCooldown(player, new Cooldown(getName(), 40L));
	}
	
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
