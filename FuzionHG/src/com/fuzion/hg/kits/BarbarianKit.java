package com.fuzion.hg.kits;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutSetSlot;

public class BarbarianKit extends Kit {
	
	HashMap<UUID, Integer> bkills = new HashMap<UUID, Integer>();
	
	public BarbarianKit() {
		super("Barbarian", "Estratégia", new ItemStack(Material.WOOD_SWORD), Group.BETA, "§7Melhore a sua espada a cada 3 kills.");
		addItem(new ItemBuilder().type(Material.WOOD_SWORD).name("Barbarian Sword").durability(45).enchantment(Enchantment.DURABILITY).build());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEnchant(PrepareItemEnchantEvent e) {
		Player p = e.getEnchanter();
		if (hasKit(p)) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.GRAY + "Você não pode encantar items com o kit " + ChatColor.RED + ChatColor.BOLD + "Barbarian" + ChatColor.GRAY + "!");
		}
	}

	@EventHandler
	public void onBigorna(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Block block = e.getClickedBlock();
		if (!hasKit(p)) {
			return;
		}
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && (block.getType() == Material.ANVIL)) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.GRAY + "Você não pode usar a bigorna com o kit " + ChatColor.RED + ChatColor.BOLD + "Barbarian" + ChatColor.GRAY + "!");
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		if (!hasKit(p)) {
			return;
		}
		if (p.getItemInHand() == null) {
			return;
		}
		if (!p.getItemInHand().hasItemMeta()) {
			return;
		}
		if (!p.getItemInHand().getItemMeta().hasDisplayName()) {
			return;
		}
		if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Barbarian Sword")) {
			p.getItemInHand().setDurability((short) 0);
			EntityPlayer eP = ((CraftPlayer) p).getHandle();
			eP.playerConnection.sendPacket(new PacketPlayOutSetSlot(eP.activeContainer.windowId, 44 - Math.abs(p.getInventory().getHeldItemSlot() - 8), CraftItemStack.asNMSCopy(p.getItemInHand())));
		}
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if ((event.getDamager() instanceof Player)) {
			Player p = (Player) event.getDamager();
			if (!hasKit(p)) {
				return;
			}
			if (p.getItemInHand() == null) {
				return;
			}
			if (!p.getItemInHand().hasItemMeta()) {
				return;
			}
			if (!p.getItemInHand().getItemMeta().hasDisplayName()) {
				return;
			}
			if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Barbarian Sword")) {
				p.getItemInHand().setDurability((short) 0);
				EntityPlayer eP = ((CraftPlayer) p).getHandle();
				eP.playerConnection.sendPacket(new PacketPlayOutSetSlot(eP.activeContainer.windowId, 44 - Math.abs(p.getInventory().getHeldItemSlot() - 8), CraftItemStack.asNMSCopy(p.getItemInHand())));
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onKill(PlayerDeathEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getEntity().getKiller() instanceof Player))) {
			Player k = e.getEntity().getKiller();
			if (!hasKit(k)) {
				return;
			}
			if (!bkills.containsKey(k.getUniqueId())) {
				bkills.put(k.getUniqueId(), 1);
			} else {
				bkills.put(k.getUniqueId(), bkills.get(k.getUniqueId()) + 1);

			}
			boolean update = false;
			int changed = 0;
			if (bkills.containsKey(k.getUniqueId())) {
				if (bkills.get(k.getUniqueId()) % 3 == 0) {
					changed++;
				}
			}
			int times = 1;
			if (changed == 1) {
				update = true;
			}
			if (update) {
				boolean sword = false;
				int slot = -1;
				for (int i = 0; i < k.getInventory().getSize(); i++) {
					ItemStack item = k.getInventory().getItem(i);
					if (item != null) {
						if (item.getType().toString().contains("_SWORD")) {
							if (item.hasItemMeta()) {
								if (item.getItemMeta().hasDisplayName()) {
									if (item.getItemMeta().getDisplayName().contains("Barbarian")) {
										sword = true;
										slot = i;
										break;
									}
								}
							}
						}
					}
				}
				if (sword) {
					for (int i = 0; i < times; i++) {
						k.getInventory().setItem(slot, update(k.getInventory().getItem(slot)));
					}
					k.sendMessage(ChatColor.GRAY + "O nivel da sua espada aumentou");
				}
			}
		}
	}

	private ItemStack update(ItemStack item) {
		if ((item.getType() == Material.DIAMOND_SWORD) && (item.getItemMeta().getDisplayName().contains("Barbarian"))) {
			item.setType(Material.DIAMOND_SWORD);
			item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		}
		if ((item.getType() == Material.IRON_SWORD) && (item.getItemMeta().getDisplayName().contains("Barbarian"))) {
			item.setType(Material.DIAMOND_SWORD);
		}
		if ((item.getType() == Material.STONE_SWORD) && (item.getItemMeta().getDisplayName().contains("Barbarian"))) {
			item.setType(Material.IRON_SWORD);
		}
		if ((item.getType() == Material.WOOD_SWORD) && (item.getItemMeta().getDisplayName().contains("Barbarian"))) {
			item.setType(Material.STONE_SWORD);
		}
		return item;
	}
}
