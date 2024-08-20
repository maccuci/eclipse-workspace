package me.feenks.pvp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import me.feenks.core.bukkit.api.item.ItemBuilder;
import me.feenks.pvp.events.PlayerSelectAbilityEvent;
import me.feenks.pvp.manager.ScoreboardManager;
import me.feenks.pvp.manager.ability.Ability;
import me.feenks.pvp.manager.ability.AbilityManager;
import me.feenks.pvp.manager.menu.AbilitiesMenu;
import me.feenks.pvp.manager.menu.ShopkerMenu;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ItemBuilder builder = new ItemBuilder();
		
		event.setJoinMessage(null);
		player.setFoodLevel(20);
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		player.getInventory().setItem(1, builder.type(Material.CHEST).name("§aHabilidades").build());
		player.getInventory().setItem(3, builder.type(Material.ENCHANTMENT_TABLE).name("§cConfigurações").build());
		player.getInventory().setItem(5, builder.type(Material.DIAMOND).name("§bVendedor").build());
		player.getInventory().setItem(7, builder.type(Material.COMPASS).name("§2Warps").build());
		
		new ScoreboardManager().createScoreboard(player);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if(ItemBuilder.checkItem(item, "§aHabilidades")) {
			AbilitiesMenu.open(player, 1);
		}
		
		if(ItemBuilder.checkItem(item, "§bVendedor")) {
			ShopkerMenu.open(player);
		}
	}
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		
		if(!(event.getRightClicked() instanceof CraftPlayer)) 
			return;
		
		CraftPlayer entityPlayer = (CraftPlayer)event.getRightClicked();
		
		if(entityPlayer.getName() == "§eDesafie o Bot!") {
			player.sendMessage("");
		}
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.NETHER_PORTAL || event.getCause() == TeleportCause.END_PORTAL)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onItemDrop(ItemSpawnEvent event) {
		event.getEntity().remove();
	}
	
	@EventHandler
	public void onSoup(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = event.getItem();
		if (item == null || item.getType() == Material.AIR)
			return;
		if (item.getType() == Material.MUSHROOM_SOUP) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (((Damageable) p).getHealth() < ((Damageable) p).getMaxHealth() || p.getFoodLevel() < 20) {
					int restores = 7;
					event.setCancelled(true);
					if (((Damageable) p).getHealth() < ((Damageable) p).getMaxHealth())
						if (((Damageable) p).getHealth() + restores <= ((Damageable) p).getMaxHealth())
							p.setHealth(((Damageable) p).getHealth() + restores);
						else
							p.setHealth(((Damageable) p).getMaxHealth());
					else if (p.getFoodLevel() < 20)
						if (p.getFoodLevel() + restores <= 20) {
							p.setFoodLevel(p.getFoodLevel() + restores);
							p.setSaturation(3);
						} else {
							p.setFoodLevel(20);
							p.setSaturation(3);
						}
					item = new ItemStack(Material.BOWL);
					p.setItemInHand(item);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCompass(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = event.getItem();
		if (item == null || item.getType() == Material.AIR)
			return;
		if (ItemBuilder.checkItem(item, "§aBússola")) {
			Player target = null;
			double distance = 10000;
			for (Player game : Bukkit.getOnlinePlayers()) {
				double distOfPlayerToVictim = p.getLocation().distance(game.getPlayer().getLocation());
				if (distOfPlayerToVictim < distance && distOfPlayerToVictim > 25) {
					distance = distOfPlayerToVictim;
					target = game;
				}
			}
			if (target == null) {
				p.sendMessage("§cA bússola não encontrou ninguém! Apontando para o spawn.");
				p.setCompassTarget(Bukkit.getWorlds().get(0).getSpawnLocation());
			} else {
				p.setCompassTarget(target.getLocation());
				p.sendMessage("§eApontando para o jogador §7" + target.getName() + "§e. Mate-o ou morra!");
			}
		}
	}
	
	@EventHandler
	public void playerSelect(PlayerSelectAbilityEvent event) {
		playerSelectAbility(event.getPlayer(), event.getAbility());
	}
	
	private void playerSelectAbility(Player player, Ability ability) {
		if(player == null)
			return;
		if(ability == null)
			return;
		new AbilityManager().send(player, ability);
		player.sendMessage("§aVocê selecionou a habilidade §f" + ability.getName() + " com sucesso.");
	}
	
	@EventHandler
	public void onCreature(CreatureSpawnEvent event) {
		if(event.getSpawnReason() != SpawnReason.CUSTOM)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
}
