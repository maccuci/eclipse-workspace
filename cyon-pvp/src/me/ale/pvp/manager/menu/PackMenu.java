package me.ale.pvp.manager.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.api.menu.ClickType;
import me.ale.commons.api.menu.MenuClickHandler;
import me.ale.commons.api.menu.MenuInventory;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.bukkit.permission.PermissionBukkitManager;
import me.ale.commons.bukkit.permission.PermissionConfig;
import me.ale.commons.core.account.PackType;
import me.ale.commons.core.account.manager.PackColletionManager;
import me.ale.commons.core.account.manager.StatsManager;
import me.ale.pvp.Main;
import me.ale.pvp.manager.achievement.type.AchievementType;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitManager;
import me.ale.pvp.manager.kit.KitRarity;

public class PackMenu {

	static final ItemBuilder builder = new ItemBuilder();
	static HashMap<String, BukkitRunnable> task = new HashMap<>();
	static HashMap<String, Integer> speed = new HashMap<>();
	static HashMap<String, Integer> time = new HashMap<>();
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§3Pack de Kits", 3);
		PackColletionManager colletionManager = new PackColletionManager();
		
		for(int i = 0; i < 27; i++) {
			menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(7).build());
		}
		menu.setItem(11, builder.type(Material.GOLD_INGOT).name("§3Pack de Kits").amount(colletionManager.get(player.getUniqueId(), "amount")).lore("§7Clique para abrir um pack de kits", "§7que pode vir de qualquer raridade.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
				if(colletionManager.get(arg0.getUniqueId(), "amount") <= 0) {
					arg0.sendMessage(CyonAPI.WARNING_PREFIX + "Você não possui nenhum pack de kits. Compre na loja ou troque com os jogadores.");
					return;
				}
				List<PackType> types = new ArrayList<>();
				for(PackType type : PackType.values()) {
					types.add(type);
				}
				int i = new Random().nextInt(types.size());
				PackType type = types.get(i);
				start(arg0, type);
				colletionManager.set(player.getUniqueId(), "amount", colletionManager.get(player.getUniqueId(), "amount") - 1);
			}
		});
		menu.open(player);
	}

	public static void start(Player player, PackType rarity) {
		StatsManager stats = BukkitMain.getPlugin().getStatsManager();
		MenuInventory menu = new MenuInventory("§3Abrindo o Pack " + ItemBuilder.captalise(rarity.name()), 3);
		for(int i = 0; i < 27; i++) {
			menu.setItem(i, builder.type(Material.STAINED_GLASS_PANE).name(" ").durability(7).build());
		}
		Main.getPlugin().getAchievementManager().giveAchievement(player, AchievementType.UNLOCK_FIRST_PACK);
		speed.put(player.getName(), Integer.valueOf(0));
	    time.put(player.getName(), Integer.valueOf(0));
	    
	    if(!task.containsKey(player.getName())) {
	    	try {
				task.put(player.getName(), (BukkitRunnable)new BukkitRunnable() {
					
					@Override
					public void run() {
						speed.put(player.getName(), Integer.valueOf(((Integer)speed.get(player.getName())).intValue() + 1));
			              if (((Integer)speed.get(player.getName())).intValue() % 20 == 0) {
			                time.put(player.getName(), Integer.valueOf(((Integer)time.get(player.getName())).intValue() + 1));
			              }
			              if (((Integer)speed.get(player.getName())).intValue() < 50) //50
			              {
			                vidroo(menu, rarity);
							player.playSound(player.getLocation(), Sound.CLICK, 1.5F, 1.5F);
			                return;
			              }
			              if (((Integer)speed.get(player.getName())).intValue() < 100) //100
			              {
			                if (((Integer)speed.get(player.getName())).intValue() % 3 == 0) {
			                  vidroo(menu, rarity);
								player.playSound(player.getLocation(), Sound.CLICK, 1.5F, 1.5F);
			                }
			                return;
			              }
			              if (((Integer)speed.get(player.getName())).intValue() < 160) //160
			              {
			                if (((Integer)speed.get(player.getName())).intValue() % 6 == 0) {
			                  vidroo(menu, rarity);
								player.playSound(player.getLocation(), Sound.CLICK, 1.5F, 1.5F);
			                }
			                return;
			              }
			              if (((Integer)speed.get(player.getName())).intValue() < 240) //240
			              {
			                if (((Integer)speed.get(player.getName())).intValue() % 10 == 0) {
			                  vidroo(menu, rarity);
								player.playSound(player.getLocation(), Sound.CLICK, 1.5F, 1.5F);
			                }
			                return;
			              }
			              if (((Integer)speed.get(player.getName())).intValue() > 240) { //240
			            	  
			            	  if(rarity == PackType.RARE) {
				            	  String kitName = menu.getItem(13).getStack().getItemMeta().getDisplayName().replace("§9", "");
				            	  if(player.hasPermission("kit." + kitName)) {
				            		  cancel();
				            		  player.sendMessage("§cVocê já possui este kit, então você ganhou 500 de money.");
				            		  stats.set(player.getUniqueId(), "money", stats.get(player.getUniqueId(), "money") + 500);
				            		  player.closeInventory();
				            		  return;
				            	  }
				            	  
				            	  player.sendMessage(CyonAPI.WARNING_PREFIX + "Você desbloqueou o kit " + KitManager.getKit(kitName).getName() + ", sua raridade é " + KitManager.getKit(kitName).getRarity().getRarityColor() + ItemBuilder.captalise(KitManager.getKit(kitName).getRarity().name()));
				            	  PermissionConfig config = new PermissionConfig();
								config.addPermissionPlayer(player.getName(), "kit." + kitName);
								PermissionBukkitManager manager = new PermissionBukkitManager();
								manager.addPermissionName(player, "kit." + kitName);
								manager.refreshPerms(player);
			            	  } else if(rarity == PackType.MYSTIC) {
			            		  String kitName = menu.getItem(13).getStack().getItemMeta().getDisplayName().replace("§c", "");
				            	  if(player.hasPermission("kit." + kitName)) {
				            		  cancel();
				            		  player.sendMessage("§cVocê já possui este kit, então você ganhou 1000 de money.");
				            		  stats.set(player.getUniqueId(), "money", stats.get(player.getUniqueId(), "money") + 1000);
				            		  player.closeInventory();
				            		  return;
				            	  }
				            	  player.sendMessage(CyonAPI.WARNING_PREFIX + "Você desbloqueou o kit " + KitManager.getKit(kitName).getName() + ", sua raridade é " + KitManager.getKit(kitName).getRarity().getRarityColor() + ItemBuilder.captalise(KitManager.getKit(kitName).getRarity().name()));
				            	  PermissionConfig config = new PermissionConfig();
				  					config.addPermissionPlayer(player.getName(), "kit." + kitName);
				  					PermissionBukkitManager manager = new PermissionBukkitManager();
				  					manager.addPermissionName(player, "kit." + kitName);
				  					manager.refreshPerms(player);
			            	  } else if(rarity == PackType.EXTRAORDINARY) {
			            		  String kitName = menu.getItem(13).getStack().getItemMeta().getDisplayName().replace("§6", "");
				            	  if(player.hasPermission("kit." + kitName)) {
				            		  cancel();
				            		  player.sendMessage("§cVocê já possui este kit, então você ganhou 1500 de money.");
				            		  stats.set(player.getUniqueId(), "money", stats.get(player.getUniqueId(), "money") + 1500);
				            		  player.closeInventory();
				            		  return;
				            	  }
				            	  player.sendMessage(CyonAPI.WARNING_PREFIX + "Você desbloqueou o kit " + KitManager.getKit(kitName).getName() + ", sua raridade é " + KitManager.getKit(kitName).getRarity().getRarityColor() + ItemBuilder.captalise(KitManager.getKit(kitName).getRarity().name()));
				            	  PermissionConfig config = new PermissionConfig();
				  					config.addPermissionPlayer(player.getName(), "kit." + kitName);
				  					PermissionBukkitManager manager = new PermissionBukkitManager();
				  					manager.addPermissionName(player, "kit." + kitName);
				  					manager.refreshPerms(player);
				            	  Main.getPlugin().getAchievementManager().giveAchievement(player, AchievementType.FIRST_KIT_EXTRAORDINARY);
				            	  
			            	  }
			            	  new BukkitRunnable() {
								
								@Override
								public void run() {
									player.closeInventory();
								}
							}.runTaskLater(Main.getPlugin(), 20);
							cancel();
			              }
					}
				}.runTaskTimer(Main.getPlugin(), 0, 1));
			} catch (Exception e) {
			}
	    }
	    menu.open(player);
	}
	
	public static void vidroo(MenuInventory inv, PackType rarity) {
		if(rarity == PackType.RARE) {
			inv.setItem(4, builder.type(Material.TRIPWIRE_HOOK).name("§bBoa Sorte").build());
		    inv.setItem(22, builder.type(Material.STAINED_GLASS_PANE).durability(14).name(" ").build());
		    
		    inv.setItem(10, inv.getItem(11));
		    inv.setItem(11, inv.getItem(12));
		    inv.setItem(12, inv.getItem(13));
		    inv.setItem(13, inv.getItem(14));
		    inv.setItem(14, inv.getItem(15));
		    inv.setItem(15, inv.getItem(16));
		    
		    List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		    
		    for(int i = 0; i < kits.size(); i++) {
		    	int ii = new Random().nextInt(kits.size());
		    	Kit kit = kits.get(ii);
		    	if(kit.getRarity() == KitRarity.RARE) {
		    		 inv.setItem(16, builder.type(kit.getIcon()).name(kit.getRarity().getRarityColor() + kit.getName()).lore(kit.getLore()).build());
		    	}
		    }
		} else if(rarity == PackType.MYSTIC) {
			inv.setItem(4, builder.type(Material.TRIPWIRE_HOOK).name("§bBoa Sorte").build());
		    inv.setItem(22, builder.type(Material.STAINED_GLASS_PANE).durability(14).name(" ").build());
		    
		    inv.setItem(10, inv.getItem(11));
		    inv.setItem(11, inv.getItem(12));
		    inv.setItem(12, inv.getItem(13));
		    inv.setItem(13, inv.getItem(14));
		    inv.setItem(14, inv.getItem(15));
		    inv.setItem(15, inv.getItem(16));
		    
		    List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		    
		    for(int i = 0; i < kits.size(); i++) {
		    	int ii = new Random().nextInt(kits.size());
		    	Kit kit = kits.get(ii);
		    	if(kit.getRarity() == KitRarity.MYSTIC) {
		    		 inv.setItem(16, builder.type(kit.getIcon()).name(kit.getRarity().getRarityColor() + kit.getName()).lore(kit.getLore()).build());
		    	}
		    }
		} else if(rarity == PackType.EXTRAORDINARY) {
			inv.setItem(4, builder.type(Material.TRIPWIRE_HOOK).name("§bBoa Sorte").build());
		    inv.setItem(22, builder.type(Material.STAINED_GLASS_PANE).durability(14).name(" ").build());
		    
		    inv.setItem(10, inv.getItem(11));
		    inv.setItem(11, inv.getItem(12));
		    inv.setItem(12, inv.getItem(13));
		    inv.setItem(13, inv.getItem(14));
		    inv.setItem(14, inv.getItem(15));
		    inv.setItem(15, inv.getItem(16));
		    
		    List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		    
		    for(int i = 0; i < kits.size(); i++) {
		    	int ii = new Random().nextInt(kits.size());
		    	Kit kit = kits.get(ii);
		    	if(kit.getRarity() == KitRarity.EXTRAORDINARY) {
		    		 inv.setItem(16, builder.type(kit.getIcon()).name(kit.getRarity().getRarityColor() + kit.getName()).lore(kit.getLore()).build());
		    	}
		    }
		}
	}
}
