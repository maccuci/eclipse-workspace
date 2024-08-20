package me.ale.pvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class TerroristKit extends Kit {
	
	public TerroristKit() {
		super("Terrorist", Material.FLINT, new ItemStack(Material.FLINT), KitRarity.RARE, Rank.SIMPLE, "§7Crie uma rajada de vento muito forte", "§7levando seus inimigos atingidos para longe e", "§7deixando eles com efeitos.");	
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
	      Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
	        @SuppressWarnings("deprecation")
			public void run() {
	          FallingBlock fb = Bukkit.getServer().getWorld("world").spawnFallingBlock(event.getClickedBlock().getLocation().add(0.0D, 15.0D, 0.0D), Material.TNT, (byte)0);
	          fb.setDropItem(false);
	        }
	      }, 100L);
	      
	      Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable()
	      {
	        public void run()
	        {
	        	Location loc = event.getClickedBlock().getLocation();
	        	loc.getWorld().createExplosion(loc.getX(), loc.getY() + 1.0D, loc.getZ(), 2.1F, false, false);
	        }
	      }, 130L);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void bldk(EntityChangeBlockEvent event) {
		if ((event.getEntity() instanceof FallingBlock)) {
			event.setCancelled(true);
			FallingBlock fb = Bukkit.getWorld("world").spawnFallingBlock(event.getEntity().getLocation(), Material.AIR,
					(byte) 0);
			fb.setVelocity(new Vector(0, 1, 0));
		}
	}
	  
	@EventHandler
	public void damage(EntityDamageEvent event) {
		if ((event.getEntity() instanceof Player)) {
			Player player = (Player) event.getEntity();
			if ((hasKit(player)) && (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) && (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
				event.setCancelled(true);
			}
		}
	}
}
