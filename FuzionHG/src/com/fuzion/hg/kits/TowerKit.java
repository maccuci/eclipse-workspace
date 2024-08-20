package com.fuzion.hg.kits;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.Kit;

import net.minecraft.server.v1_7_R4.EntityPlayer;

public class TowerKit extends Kit {
	
	public TowerKit() {
		super("Tower", "Torre", new ItemStack(Material.BRICK), Group.BETA, "§7Destrúa terras rapidamente e com o seu dano de queda dê dano nos inimigos ao redor.");
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			
			if(event.getCause() == DamageCause.FALL) {
				if(!hasKit(player))
					return;
				
				for(Entity entity : player.getNearbyEntities(3, 3, 3)) {
					if(entity instanceof Player) {
						Player stomps = (Player)entity;
						if(!stomps.isSneaking()) {
							stomps.damage(event.getDamage() * 1.2D, player);
						}
					}
				}
				if(event.getDamage() > 4.0D) {
					event.setDamage(4.0D);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(BlockDamageEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			if (GameManager.isPreGame()) {
				return;
			}
			if (e.getBlock().getType() == Material.DIRT) {
				boolean drop = true;
				EntityPlayer p2 = ((CraftPlayer) p).getHandle();
				if (p2.getHealth() < p2.getMaxHealth()) {
					double hp = p2.getHealth() + 1;
					if (hp > p2.getMaxHealth())
						hp = p2.getMaxHealth();
					p.setHealth((int) hp);
					drop = false;
				} else if (p.getFoodLevel() < 20) {
					p.setFoodLevel(p.getFoodLevel() + 1);
					drop = false;
				} else if (p2.getHealth() > 19) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 2, 1));
					drop = false;
				}
				drop = true;
				e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, Material.DIRT.getId());
				e.getBlock().setType(Material.AIR);
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 1));

				if (!drop)
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 0, 0.5), new ItemStack(Material.DIRT));
			}
		}
	}
}
