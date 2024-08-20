package me.ale.pvp.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.bukkit.permission.PermissionBukkitManager;
import me.ale.commons.bukkit.permission.PermissionConfig;
import me.ale.commons.core.account.manager.PackColletionManager;
import me.ale.commons.core.account.manager.StatsManager;
import me.ale.pvp.Main;
import me.ale.pvp.event.PlayerBuyKitEvent;
import me.ale.pvp.event.PlayerSelectKit;
import me.ale.pvp.event.WarpSelectEvent;
import me.ale.pvp.kits.MinionsKit;
import me.ale.pvp.manager.ScoreboardManager;
import me.ale.pvp.manager.achievement.type.AchievementType;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitManager;
import me.ale.pvp.manager.menu.KitMenu;
import me.ale.pvp.manager.menu.KitType;
import me.ale.pvp.manager.menu.ProfileMenu;
import me.ale.pvp.manager.menu.PvPWarpsMenu;
import me.ale.pvp.manager.warp.Warp;
import me.ale.pvp.manager.warp.WarpManager;
import me.ale.pvp.manager.menu.PackMenu;

public class PlayerListener implements Listener {
	
	ItemBuilder builder = new ItemBuilder();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(player.isOp()) {
			event.setJoinMessage(CyonAPI.SERVER_PREFIX + "Bem-vindo §4" + player.getName() + " §7ao servidor!");
		} else {
			event.setJoinMessage(null);
		}
		
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		player.getInventory().setItem(2, builder.type(Material.ENCHANTMENT_TABLE).name("§2PvP Warps").build());
		player.getInventory().setItem(4, builder.type(Material.CHEST).name("§3Seus Kits").build());
		player.getInventory().setItem(6, builder.type(Material.SKULL_ITEM).durability(3).name("§bPerfil").skin(player.getName()).build());
		new ScoreboardManager().createScoreboard(player);
		KitMenu.kitNameSelection.remove(player);
		KitMenu.kitNameCheck.remove(player);
		new MinionsKit().removeWolfs(player);
		new PackColletionManager().set(player.getUniqueId(), "amount", 5);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) throws EventException {
		Player player = event.getPlayer();
		
		event.setFormat("%s §8» §7" + event.getMessage().replace("%", "%%"));
		
		if(KitMenu.kitNameCheck.contains(player)) {
			
			if(!KitMenu.validateName(event.getMessage())) {
				event.setCancelled(true);
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você não pode colocar caractéris especiais. Tente novamente.");
				return;
			}
			
				KitMenu.kitNameCheck.remove(player);
				KitMenu.kitNameSelection.put(player, event.getMessage());
				event.setCancelled(true);
				KitMenu.openSearch(player, KitMenu.getMenuOld(), 1, event.getMessage());
				return;
			
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		//boolean check = event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK;
		
		if(ItemBuilder.checkItem(item, "§3Seus Kits")) {
			KitMenu.open(player, 1, KitType.ALPHABETICAL);
			return;
		}
		
		if(ItemBuilder.checkItem(item, "§2PvP Warps")) {
			PvPWarpsMenu.open(player);
			return;
		}
		
		if(ItemBuilder.checkItem(item, "§bPerfil")) {
			ProfileMenu.open(player);
			return;
		}
		
		if(ItemBuilder.checkItem(item, "§aClique para receber seu kit")) {
			new KitManager().sendKit(player, new KitManager().getKit(player.getUniqueId()));
			return;
		}
	}
	
	@EventHandler
	public void onInteractBlock(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if(block.getType() == Material.NOTE_BLOCK) {
			event.setCancelled(true);
			PackMenu.open(player);
			return;
		}
	}
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
/*		Player player = event.getPlayer();
		Player clicked = (Player)event.getRightClicked();
		ItemStack item = player.getItemInHand();
		
		if(ItemBuilder.checkItem(item, "§cBastão de Troca")) {
			//TradeMenu.open(player, clicked);
		}*/
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		Kit kit = new KitManager().getKit(player.getUniqueId());
		
		if(kit.getItem() == item || kit.hasKit(player)) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
	
	@EventHandler
	public void onExplode(ExplosionPrimeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onSelectKit(PlayerSelectKit event) {
		if(event.getPlayer() == null)
			return;
		if(event.getKit() == null)
			return;
		
		Player player = event.getPlayer();
		Kit kit = event.getKit();
		new KitManager().setKit(player.getUniqueId(), kit);
		player.sendMessage(CyonAPI.SERVER_PREFIX + "Você selecionou o kit §a" + kit.getName() + ".");
	}
	
	@EventHandler
	public void onWarp(WarpSelectEvent event) {
		if(event.getPlayer() == null)
			return;
		if(event.getWarp() == null)
			return;
		
		Player player = event.getPlayer();
		Warp warp = event.getWarp();
		
		WarpManager.senWarp(player, warp);
		player.sendMessage("§aVocê foi enviado para warp §f" + ItemBuilder.captalise(warp.name()));
	}
	
	@EventHandler
	public void onBuy(PlayerBuyKitEvent event) {
		if(event.getPlayer() == null)
			return;
		if(event.getKit() == null)
			return;
		if(event.getPrice() == null)
			return;
		
		Player player = event.getPlayer();
		Kit kit = event.getKit();
		Integer price = event.getPrice();
		StatsManager stats = BukkitMain.getPlugin().getStatsManager();
		
		if(stats.get(player.getUniqueId(), "money") < price) {
			player.sendMessage(CyonAPI.WARNING_PREFIX + "Ainda lhe faltam " + (price - stats.get(player.getUniqueId(), "money") + " de money para comprar o kit " + kit.getName()));
			return;
		}
		if(player.hasPermission("kit." + kit.getName())) {
			player.sendMessage(CyonAPI.WARNING_PREFIX + "Você já possui este kit.");
			return;
		}
		stats.set(player.getUniqueId(), "money", stats.get(player.getUniqueId(), "money") - price);
		PermissionConfig config = new PermissionConfig();
		config.addPermissionPlayer(player.getName(), "kit." + kit.getName());
		PermissionBukkitManager manager = new PermissionBukkitManager();
		manager.addPermissionName(player, "kit." + kit.getName());
		manager.refreshPerms(player);
		player.sendMessage("§aVocê comprou o kit " + kit.getName() + " com sucesso.");
		Main.getPlugin().getAchievementManager().giveAchievement(player, AchievementType.FIRST_KIT_BUYED);
	}
	
	@EventHandler
	public void onReach(EntityDamageByEntityEvent event) {
		if((event.getDamager() instanceof Player)) {
			Player damager = (Player)event.getDamager();
		      Entity damaged = event.getEntity();
		      double damager_reach = damager.getLocation().distance(damaged.getLocation());
		      
		      if(damager_reach > 3.0) {
		    	  event.setCancelled(true);
		      }
		}
	}
}
