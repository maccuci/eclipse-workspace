package me.ale.pvp.kits;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
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

public class WizardKit extends Kit {
	
	public WizardKit() {
		super("Wizard", Material.DIAMOND_HOE, new ItemStack(Material.DIAMOND_HOE), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Tenha controle sob as magias §5Negra §7e §cVida§7.");
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!hasKit(player))
			return;
		ItemStack item = player.getItemInHand();
		if (item == null)
			return;
		ItemStack ITEM = getItem();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.hasCooldown(player, getName() + "Heal") || CooldownAPI.hasCooldown(player, getName() + "Black")) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}
		
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(event.getItem().getType() == Material.DIAMOND_HOE) {
				player.setItemInHand(new ItemBuilder().type(Material.GOLD_HOE).name("§cMagia da Vida").build());
				player.updateInventory();
			} else if(event.getItem().getType() == Material.GOLD_HOE) {
				player.setItemInHand(new ItemBuilder().type(Material.DIAMOND_HOE).name("§5Magia Negra").build());
				player.updateInventory();
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(event.getItem().getType() == Material.DIAMOND_HOE) {
				Location loc = player.getEyeLocation();
				BlockIterator bta = new BlockIterator(loc, 0.0D, 50);
				while (bta.hasNext()) {
					Location bt = bta.next().getLocation();
					player.getWorld().playEffect(bt, Effect.STEP_SOUND, Material.COAL, 15);
					player.playSound(bt, Sound.GHAST_FIREBALL, 3.0F, 3.0F);
				}
				Snowball s = player.launchProjectile(Snowball.class);
				Vector v = player.getLocation().getDirection().normalize().multiply(10);
				s.setVelocity(v);
				s.setMetadata("magicBlack", new FixedMetadataValue(Main.getPlugin(), true));
				player.setVelocity(player.getEyeLocation().getDirection().multiply(-0.6F));
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você disparou o seu beam com sucesso.");
				CooldownAPI.addCooldown(player, new Cooldown(getName() + "Black", 30L));
			} else if(event.getItem().getType() == Material.GOLD_SPADE) {
				new BukkitRunnable() {
					
					@Override
					public void run() {
						a(player);
					}
				}.runTaskTimer(Main.getPlugin(), 40L, 60L);
				
				
//				Potion potion = new Potion(PotionType.INSTANT_HEAL, 3);
//				potion.setSplash(true);
//				
//				ItemStack itemStack = new ItemStack(Material.POTION);
//				potion.apply(itemStack);
//				
//				ThrownPotion thrownPotion = (ThrownPotion)player.getWorld().spawnEntity(player.getLocation(), EntityType.SPLASH_POTION);
//				thrownPotion.setItem(itemStack);
				
				CooldownAPI.addCooldown(player, new Cooldown(getName() + "Heal", 30L));
			}
		}
	}
	
	@EventHandler
	public void onDamageLaser(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) event.getDamager();
			if (s.getShooter() instanceof Player) {
				if (s.hasMetadata("magicBlack")) {
					Player damaged = (Player) event.getEntity();
					damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0));
					damaged.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, 0));
					damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 5 * 20, 0));
					damaged.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 0));
					event.setDamage(3);
				}
			}
		}
	}
	
	public void a(Player player) {
		double x = 0.0D;
        double z = 0.0D;
        for (int y = 0; y <= 7; y++)
        {
          switch (y)
          {
          case 0: 
            z = -1.0D;
            x = 0.1D;
            break;
          case 1: 
            z = -1.0D;
            x = 0.9D;
            break;
          case 2: 
            x = 1.0D;
            z = 0.1D;
            break;
          case 3: 
            x = 1.0D;
            z = 0.9D;
            break;
          case 4: 
            z = 1.0D;
            x = 0.1D;
            break;
          case 5: 
            z = 1.0D;
            x = 0.9D;
            break;
          case 6: 
            x = -1.0D;
            z = 0.1D;
            break;
          case 7: 
            x = -1.0D;
            z = 0.9D;
          }
          Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + x, player.getLocation().getY() + 1.5D, player.getLocation().getBlockZ() + z);
          Potion potion = new Potion(PotionType.INSTANT_HEAL, 1);
          potion.setSplash(true);
          
          ItemStack is = new ItemStack(Material.POTION);
          potion.apply(is);
          
          ThrownPotion p = (ThrownPotion)player.getWorld().spawnEntity(loc, EntityType.SPLASH_POTION);
          p.setItem(is);
        }
    }
}
