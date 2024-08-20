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

public class IncendiaryKit extends Kit {
	
	public IncendiaryKit() {
		super("Incendiary", Material.BLAZE_ROD, new ItemStack(Material.BLAZE_ROD), KitRarity.RARE, Rank.SIMPLE, "�7Crie uma rajada de fogo muito forte", "�7incediando seus oponentes e retirando", "�7muita vida dos mesmos.");
	}
	
	@EventHandler
	public void onSnail(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (!hasKit(p))
			return;
		Action a = event.getAction();
		ItemStack item = p.getItemInHand();
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
		p.updateInventory();
		if (CooldownAPI.hasCooldown(p, getName())) {
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			p.sendMessage(CyonAPI.SERVER_PREFIX + "Voc� est� em cooldown durante �c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(p).getRemaining()));
			return;
		}
		Location loc = p.getEyeLocation();
		BlockIterator bta = new BlockIterator(loc, 0.0D, 50);
		while (bta.hasNext()) {
			Location bt = bta.next().getLocation();
			p.getWorld().playEffect(bt, Effect.STEP_SOUND, Material.REDSTONE_BLOCK, 15);
			p.playSound(bt, Sound.GHAST_FIREBALL, 3.0F, 3.0F);
		}
		Snowball s = p.launchProjectile(Snowball.class);
		Vector v = p.getLocation().getDirection().normalize().multiply(10);
		s.setVelocity(v);
		s.setMetadata("incendiary", new FixedMetadataValue(Main.getPlugin(), true));
		p.setVelocity(p.getEyeLocation().getDirection().multiply(-0.6F));
		p.sendMessage(CyonAPI.SERVER_PREFIX + "Voc� disparou o seu beam com sucesso.");
		CooldownAPI.addCooldown(p, new Cooldown(getName(), 30L));
	}

	@EventHandler
	public void onDamageLaser(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) e.getDamager();
			if (s.getShooter() instanceof Player) {
				if (s.hasMetadata("incendiary")) {
					Player damaged = (Player) e.getEntity();
					damaged.setFireTicks(100);
					damaged.setVelocity(damaged.getVelocity().add(new Vector(0, 1.2, 0)));
					e.setDamage(4);
				}
			}
		}
	}
}
