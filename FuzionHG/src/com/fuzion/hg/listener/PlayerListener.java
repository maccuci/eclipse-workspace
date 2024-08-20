package com.fuzion.hg.listener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.api.admin.AdminAPI.AdminMode;
import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.json.JSONChatExtra;
import com.fuzion.core.api.json.JSONChatHoverEventType;
import com.fuzion.core.api.json.JSONChatMessage;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.hg.Main;
import com.fuzion.hg.event.GameStageChangeEvent;
import com.fuzion.hg.event.PlayerSelectKitEvent;
import com.fuzion.hg.event.SpectorJoinEvent;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.game.GameStage;
import com.fuzion.hg.manager.kit.Kit;
import com.fuzion.hg.manager.kit.KitManager;
import com.fuzion.hg.manager.timer.GameTimer;
import com.fuzion.hg.manager.timer.InvincibilityTimer;
import com.fuzion.hg.manager.winner.WinnerManager;

import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener {
	
	private Set<UUID> joined = new HashSet<UUID>();
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		player.setFoodLevel(20);
		GroupManager groupManager = new GroupManager();
		WinnerManager manager = Main.getPlugin().getWinnerManager();
		if(GameManager.isPreGame())
			return;
		if(groupManager.hasGroupPermission(player.getUniqueId(), Group.TRIAL) && manager.hasWinner(player))
			return;
		if(groupManager.hasGroupPermission(player.getUniqueId(), Group.ULTRA)  && manager.hasWinner(player))
			return;
		if(groupManager.hasGroupPermission(player.getUniqueId(), Group.ALPHA) && manager.hasWinner(player) && !DeathListener.relogProcess.contains(player.getUniqueId()) && !joined.contains(player.getUniqueId()) && GameTimer.timer < 300)
			return;
		if(DeathListener.getDeathMessages().containsKey(player.getUniqueId()) && !(new GroupManager().hasGroupPermission(player.getUniqueId(), Group.PREMIUM)))
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, DeathListener.getDeathMessages().get(player.getUniqueId()));
		else
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cA partida já começou!\n§6Adquira vip para espectar.");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		GroupManager groupManager = new GroupManager();
		if(Main.getPlugin().getWinnerManager().hasWinner(player)) {
			player.sendMessage("§aParabéns! Você ganhou a partida passada, e com isso você ganhou a tag §2§lWINNER §ae todos os kits. Mas só durante esta partida.");
		}
		if (DeathListener.relogProcess.contains(player.getUniqueId()))
			return;
		if(GameManager.isGame()) {
			if(GameTimer.timer <= 300 && !joined.contains(player.getUniqueId())) {
				player.getInventory().clear();
				for (PotionEffect effect : player.getActivePotionEffects())
					player.removePotionEffect(effect.getType());
				player.getInventory().addItem(new ItemStack(Material.COMPASS, 1));
				joined.add(player.getUniqueId());
				player.sendMessage("§eVocê entrou na partida.");
			} else {
				event.setJoinMessage(null);
				if(groupManager.hasGroupPermission(player.getUniqueId(), Group.TRIAL)) {
					new AdminAPI().updateMode(player, AdminMode.ADMIN);
				} else {
					GameManager.addSpector(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onReceipe(CraftItemEvent event) {
		if (!(event.getView().getPlayer() instanceof Player))
			return;
		Player player = (Player) event.getView().getPlayer();
		Kit kit = new KitManager().getKit(player);
		if (kit == null)
			return;
		for (ItemStack item : event.getInventory().getContents()) {
			if (item == null)
				continue;
			if(kit.isKitItem(kit, item)) {
				event.setCancelled(true);
				break;
			}
		}
	}
	
	@EventHandler
	public void onInventoryMove(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
				|| event.getClickedInventory() == event.getInventory()) {
			ItemStack currentItem = event.getCursor();
			if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				if (event.getClickedInventory().getItem(event.getSlot()) != null) {
					currentItem = event.getClickedInventory().getItem(event.getSlot());
				}
			}
			if (currentItem.getType() != Material.AIR) {
				Player player = (Player) event.getWhoClicked();
				Kit kit = new KitManager().getKit(player);
				if (kit == null)
					return;
					if (kit.isKitItem(kit, currentItem)) {
						event.setCancelled(true);
						player.updateInventory();
						return;
					}
				}
			}
		}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		Kit kit = new KitManager().getKit(player);
		ItemStack item = event.getItemDrop().getItemStack();
		if (item == null)
			return;
		if (kit == null)
			return;
			if (kit.isKitItem(kit, item)) {
				event.setCancelled(true);
				player.updateInventory();
				return;
		}
	}
	
	@EventHandler
	public void onKit(PlayerSelectKitEvent event) {
		if(event.getKit() == null)
			return;
		Player player = event.getPlayer();
		Kit kit = event.getKit();
		new KitManager().setKit(player, kit);
		player.sendMessage("§aVocê selecionou o kit " + kit.getName() + " com sucesso.");
	}
	
	@SuppressWarnings("deprecation")
	public static void prepareRecipes() {
		newShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP), Arrays.asList(new MaterialData(Material.CACTUS), new MaterialData(Material.BOWL)));
		newShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP), Arrays.asList(new MaterialData(Material.INK_SACK, (byte) 3), new MaterialData(Material.BOWL)));
		newShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP), Arrays.asList(new MaterialData(Material.SUGAR), new MaterialData(Material.BOWL)));
		newShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP), Arrays.asList(new MaterialData(Material.SEEDS), new MaterialData(Material.SEEDS), new MaterialData(Material.BOWL)));
		ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(Material.GRASS));
		recipe.addIngredient(Material.DIRT);
		recipe.addIngredient(Material.SEEDS);
		Bukkit.addRecipe(recipe);
	}
	
	public static void newShapelessRecipe(ItemStack result, List<MaterialData> materials) {
		ShapelessRecipe recipe = new ShapelessRecipe(result);
		for (MaterialData mat : materials) {
			recipe.addIngredient(mat);
		}
		Bukkit.addRecipe(recipe);
	}
	
	@EventHandler
	public void onChange(GameStageChangeEvent event) {
		if(event.getNewStage() == GameStage.WAITING) {
			return;
		}
		GameManager.setStage(event.getNewStage());
		switch (event.getNewStage()) {
		case INVINCIBILITY:
			new InvincibilityTimer().pulse();
			break;
			
		case GAME:
			new GameTimer().pulse();
			break;

		default:
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCompass(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (GameManager.getStage() == GameStage.WAITING) {
			return;
		}
		ItemStack item = event.getItem();
		if (item == null || item.getType() == Material.AIR)
			return;
		if (item.getType() == Material.COMPASS) {
			Player target = null;
			double distance = 10000;
			for (Player game : Bukkit.getOnlinePlayers()) {
				if (GameManager.isSpector(game))
					continue;
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
	
	@EventHandler
	public void onSpect(SpectorJoinEvent event) {
		Player player = event.getPlayer();
		GameManager.addSpector(player);
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		player.getInventory().addItem(new ItemBuilder().type(Material.CHEST).name("§aJogadores").build());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		TagManager tagManager = new TagManager(player);
		GroupManager groupManager = new GroupManager();
		if(CooldownAPI.isInCooldown(player.getUniqueId(), "chat")) {
			player.sendMessage("§cAguarde ainda " + DateUtils.formatDifference(CooldownAPI.getTimeLeft(player.getUniqueId(), "chat")));
			event.setCancelled(true);
			return;
		}
		
		if(GameManager.isSpector(player)) {
			return;
		}
		
		JSONChatMessage chatMessage = new JSONChatMessage("", null, null);
		JSONChatExtra nickname = new JSONChatExtra(tagManager.getTag().getName() + player.getName());
		nickname.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, "§7Informações sobre §6" + player.getName() + "\n\n§7Grupo: " + groupManager.getGroup(player.getUniqueId()).getColor() + groupManager.getGroup(player.getUniqueId()).name());
		chatMessage.addExtra(nickname);
		JSONChatExtra message = new JSONChatExtra(ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());
		message.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, ChatColor.GREEN + "Mensagem enviada por " + ChatColor.WHITE + player.getName() + ChatColor.GREEN + " na hora " + ChatColor.WHITE + new SimpleDateFormat("HH:mm:ss").format(new Date()));
		chatMessage.addExtra(message);
		for(Player online : Bukkit.getOnlinePlayers()) {
			chatMessage.sendToPlayer(online);
		}
		if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.YOUTUBERPLUS)) {
			CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), "chat", 5);
			cooldown.start();
		}
		event.setCancelled(true);
		return;
	}
}
