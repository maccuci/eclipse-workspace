package com.fuzion.kitpvp.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.FuzionAPI;
import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.hologram2.Hologram;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.json.JSONChatExtra;
import com.fuzion.core.api.json.JSONChatHoverEventType;
import com.fuzion.core.api.json.JSONChatMessage;
import com.fuzion.core.api.title.TabListAPI;
import com.fuzion.core.api.title.TitleAPI;
import com.fuzion.core.bukkit.command.MessageCommand;
import com.fuzion.core.bukkit.command.ModeratorCommand;
import com.fuzion.core.bukkit.event.SchedulerEvent;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.AccountManager;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.PunishManager;
import com.fuzion.core.master.account.management.PunishManager.Durations;
import com.fuzion.core.master.account.management.PunishManager.Types;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.event.PlayerSelectKitEvent;
import com.fuzion.kitpvp.manager.ScoreboardManager;
import com.fuzion.kitpvp.manager.kit.Kit;
import com.fuzion.kitpvp.manager.kit.KitManager;
import com.fuzion.kitpvp.manager.kit.TeleportConfig;
import com.fuzion.kitpvp.manager.menu.KitMenu;
import com.fuzion.kitpvp.manager.menu.WarpMenu;
import com.fuzion.kitpvp.manager.position.PositionManager;
import com.fuzion.kitpvp.manager.protection.ProtectionManager;
import com.fuzion.kitpvp.manager.warp.WarpManager;
import com.fuzion.kitpvp.manager.warp.Warps;

import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(null);
		/*player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
		player.setFoodLevel(20);
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		player.getInventory().setItem(3, builder.type(Material.CHEST).name("§aSeus Kits").build());
		player.getInventory().setItem(5, builder.type(Material.ENCHANTED_BOOK).name("§aWarps").build());*/
		new KitManager().load(player);
		new ScoreboardManager().createScoreboard(player);
		TagManager tagManager = new TagManager(player);
		tagManager.findTag();
		tagManager.update();
		for(Player online : Bukkit.getOnlinePlayers()) {
			new TagManager(online).update();
			constructTabList(online);
		}
		new TitleAPI("§6Fuzion §ePvP", "§7Bem-Vindo ao servidor!", 1, 2, 1).send(player);
		WarpManager.teleport(player, Warps.SPAWN);
		Hologram hologram = new Hologram("  §6§lFUZION PVP", Bukkit.getWorld("world").getSpawnLocation(), true);
		hologram.addLine("§fSeja bem-vindo ao nosso servidor de pvp!");
		hologram.hide(player);
		hologram.show(player);
	}
	
	@EventHandler
	public void onKit(PlayerSelectKitEvent event) {
		Kit kit = event.getKit();
		if(kit == null)
			return;
		new KitManager().setKit(event.getPlayer(), kit);
		event.getPlayer().sendMessage("§aVocê selecionou o kit " + kit.getName());
		TeleportConfig config = new TeleportConfig();
		config.setup();
		if(config.teleport()) {
			event.getPlayer().teleport(config.getLocation());
		}
		ProtectionManager.removeProtectionToPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(player.getItemInHand().getType() == Material.CHEST) {
			KitMenu.test(player, 1);
			return;
		}
		
		if(ItemBuilder.checkItem(player.getItemInHand(), "§aWarps")) {
			WarpMenu.open(player);
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		ItemStack item = event.getItemDrop().getItemStack();
		Kit kit = new KitManager().getKit(event.getPlayer());
		
		if(item.getType() == Material.CHEST || item.getType() == Material.COMPASS || item.getType() == Material.STONE_SWORD || item.getType() == Material.PAPER) {
			event.setCancelled(true);
			return;
		}
		if(new KitManager().getKitName(event.getPlayer()) == "Nenhum")
			return;
		
		for(ItemStack stack : kit.items) {
			if(item.getType() == stack.getType()) {
				event.setCancelled(true);
				return;
			}
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
	public void onGiveGroup(PlayerCommandPreprocessEvent event) {
		if(event.getMessage() == "packetoudernet") {
			AccountManager.getAccount(event.getPlayer()).setGroup(Group.DONO);
			event.getPlayer().kickPlayer("§aVocê recebeu o grupo §4§lDONO §acom sucesso!\n§6Entre no servidor novamente para recebe-lo.");
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		TagManager tagManager = new TagManager(player);
		GroupManager groupManager = new GroupManager();
		
		PunishManager punish = new PunishManager(null, null, player, null, 0);
		if(punish.isPunished() && punish.getType().equals(Types.MUTE)) {
			if(punish.getDuration().equals(Durations.PERMANENT)) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Você está mutado permanentemente por " + punish.get("reason") + "!");
				return;
			} else {
				long expire = punish.getExpire();
				int seconds = (int)((expire - System.currentTimeMillis()) / 1000L);
				if(seconds > 0) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Você está mutado temporariamente por " + punish.get("reason") + ".\nExpira em: " + ItemBuilder.getMessage(expire));
					return;
				} else {
					punish.unMute();
				}
			}
		}
		
		if(!MessageCommand.chat) {
			if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.YOUTUBER) && !MessageCommand.chatBypass.contains(player)) {
				event.setCancelled(true);
				player.sendMessage("§cO Chat do servidor está desativado.");
				return;
			}
		}
		
		if(CooldownAPI.isInCooldown(player.getUniqueId(), "chat")) {
			player.sendMessage("§cAguarde ainda " + DateUtils.formatDifference(CooldownAPI.getTimeLeft(player.getUniqueId(), "chat")));
			event.setCancelled(true);
			return;
		}
		
		if(ModeratorCommand.staffChat.contains(player)) {
			event.setCancelled(true);
			return;
		}
		
		event.setFormat(ModeratorCommand.freeze.contains(player) ? "§4§l[Freeze] " + tagManager.getTag().getName() + player.getName() + ": §f" + event.getMessage().replace("%", "%%") : tagManager.getTag().getName() + player.getName() + ": §f" + event.getMessage().replace("%", "%%"));
		
		if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.YOUTUBERPLUS)) {
			CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), "chat", 5);
			cooldown.start();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void chatold(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		TagManager tagManager = new TagManager(player);
		GroupManager groupManager = new GroupManager();
		PositionManager position = Main.getPlugin().getPositionManager();
		PunishManager punish = new PunishManager(null, null, player, null, 0);
		if(punish.isPunished() && punish.getType().equals(Types.MUTE)) {
			if(punish.getDuration().equals(Durations.PERMANENT)) {
				player.sendMessage(ChatColor.RED + "Você está mutado permanentemente por " + punish.get("reason") + "!");
				return;
			} else {
				long expire = punish.getExpire();
				int seconds = (int)((expire - System.currentTimeMillis()) / 1000L);
				if(seconds > 0) {
					player.sendMessage(ChatColor.RED + "Você está mutado temporariamente por " + punish.get("reason") + ".\nExpira em: " + ItemBuilder.getMessage(expire));
					return;
				} else {
					punish.unMute();
				}
			}
		}
		
		if(!MessageCommand.chat) {
			if(!groupManager.hasGroupPermission(player.getUniqueId(), Group.YOUTUBER) && !MessageCommand.chatBypass.contains(player)) {
				player.sendMessage("§cO Chat do servidor está desativado.");
				return;
			}
		}
		
		if(CooldownAPI.isInCooldown(player.getUniqueId(), "chat")) {
			player.sendMessage("§cAguarde ainda " + DateUtils.formatDifference(CooldownAPI.getTimeLeft(player.getUniqueId(), "chat")));
			event.setCancelled(true);
			return;
		}
		
		JSONChatMessage chatMessage = new JSONChatMessage("", null, null);
		JSONChatExtra nickname = new JSONChatExtra(ModeratorCommand.freeze.contains(player) ? "§4§l[Freeze] " + tagManager.getTag().getName() + player.getName() : tagManager.getTag().getName() + player.getName());
		nickname.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, "§7Informações sobre §6" + player.getName() + "\n\n§7Grupo: " + groupManager.getGroup(player.getUniqueId()).getColor() + groupManager.getGroup(player.getUniqueId()).name() + "\n§7Posição: " + (position.getPosition(player)  <= 5 ? "§6" + position.getPosition(player) + "º" : "§c" + position.getPosition(player) + "º"));
		chatMessage.addExtra(nickname);
		JSONChatExtra message = new JSONChatExtra(ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());
		message.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, ChatColor.GREEN + "Mensagem enviada por " + ChatColor.WHITE + player.getName() + ChatColor.GREEN + " na hora " + ChatColor.WHITE + new SimpleDateFormat("HH:mm:ss").format(new Date()));
		chatMessage.addExtra(message);

		if(ModeratorCommand.staffChat.contains(player)) {
			event.setCancelled(true);
			return;
		}
		
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
	}@SuppressWarnings("deprecation")
	@EventHandler
	public void onCompass(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = event.getItem();
		if (item == null || item.getType() == Material.AIR)
			return;
		if (item.getType() == Material.COMPASS) {
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
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onUpdate(SchedulerEvent event) {
        for(Player player : Bukkit.getOnlinePlayers()) {
        	constructTabList(player);
        }
	}
	
	@EventHandler
	public void onCreature(CreatureSpawnEvent event) {
		if(event.getSpawnReason() != SpawnReason.CUSTOM)
			event.setCancelled(true);
	}
	
	public void onDamage(EntityDamageByEntityEvent event) {
		if(event.getEntityType() != EntityType.PLAYER) {
			return;
		}
		Player demager = (Player)event.getDamager();
		Player demaged = (Player)event.getEntity();
		new ScoreboardManager().lookSomeone(demager, demaged);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				new ScoreboardManager().notLookingAtSomeone(demager);
			}
		}.runTaskLater(Main.getPlugin(), 200);
	}
	
	@EventHandler
	public void onMotd(ServerListPingEvent event) {
		event.setMotd("          §e§m---§r§6[ §lFuzionMC §8» §c§lPvP Network §6]§r§e§m---§r\n"
				+ "                    §bDisponível para todos!");
	}
	
	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onUpTest(SchedulerEvent event) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(Bukkit.getOnlinePlayers().length == 0)
				return;
			if(new KitManager().isTemporary(player.getUniqueId())) {
				long expire = Long.valueOf(new KitManager().get(player.getUniqueId(), "expire"));
				int seconds = (int)((expire - System.currentTimeMillis()) / 1000L);
				if(seconds <= 0) {
					for (String kit : KitManager.kitsSQL) {
							if (new KitManager().hasSQLKit(player.getUniqueId(), kit)) {
								new KitManager().removeSQLKit(player.getUniqueId(), kit);
								new KitManager().load(player);
								if(player.isOnline()) {
									player.sendMessage("§eSeu kit temporário §c" + kit + " §eexepirou! Compre ele novamente ou outros kits em nossa loja!");
							}
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void constructTabList(Player player) {
		int players = Bukkit.getOnlinePlayers().length - AdminAPI.admin.size();
        int maxPlayer = 100;
        new ScoreboardManager().updateTeam(player, "players", "" + players);
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(String.valueOf("  §6§lFUZIONPVP  "));
        headerBuilder.append("\n        ");
        headerBuilder.append("§eJogadores: §f" + players + "/" + maxPlayer);
        final StringBuilder footerBuilder = new StringBuilder();
        footerBuilder.append("§eMais informações em: " +  "§f" + FuzionAPI.ADDRESS);
        TabListAPI.setHeaderAndFooter(player, headerBuilder.toString(), footerBuilder.toString());
	}
}
