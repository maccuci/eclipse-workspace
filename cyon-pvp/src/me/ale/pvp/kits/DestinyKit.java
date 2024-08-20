package me.ale.pvp.kits;

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

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class DestinyKit extends Kit {
	
	public DestinyKit() {
		super("Destiny", Material.BOOKSHELF, new ItemStack(Material.BOOKSHELF), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Lance um raio e ao atingir um jogador", "§7lance ele para cima e o mesmo ganhará", "§7efeitos ruins e receberá §c2 §7de dano.");
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
			player.getWorld().playEffect(bt, Effect.STEP_SOUND, Material.REDSTONE_ORE, 15);
			player.playSound(bt, Sound.GHAST_FIREBALL, 3.0F, 3.0F);
		}
		Snowball s = player.launchProjectile(Snowball.class);
		Vector v = player.getLocation().getDirection().normalize().multiply(10);
		s.setVelocity(v);
		s.setMetadata("destiny", new FixedMetadataValue(Main.getPlugin(), true));
		player.setVelocity(player.getEyeLocation().getDirection().multiply(-0.6F));
		player.sendMessage(CyonAPI.SERVER_PREFIX + "Você disparou o seu raio com sucesso.");
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 30L));
	}
	
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
