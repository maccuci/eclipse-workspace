package com.fuzion.hg.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.Kit;

import net.md_5.bungee.api.ChatColor;

public class JackhamerKit extends Kit {
	
	public static HashMap<String, Integer> used = new HashMap<String, Integer>();
	
	public JackhamerKit() {
		super("Jackhamer", "Trap", new ItemStack(Material.STONE_AXE), Group.BETA, "§7Que apenas um bloco e todos abaixo do mesmo irão sumir.");
		addItem(new ItemStack(Material.STONE_AXE, 1));
	}
	
	@EventHandler
	public void Jack(BlockBreakEvent e) {
		Player p = e.getPlayer();
		final Block bloco = e.getBlock();
		final Block cima = bloco.getRelative(BlockFace.UP);
		final Block baixo = bloco.getRelative(BlockFace.DOWN);
		if (p.getItemInHand().getType() == Material.STONE_AXE) {
			if (hasKit(p)) {
				if (GameManager.isInvincibility()) {
					p.sendMessage(ChatColor.RED + "Você não pode usar na invencibilidade.");
					return;
				}
				if (CooldownAPI.isInCooldown(p.getUniqueId(), getName())) {
					p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
					p.sendMessage("§cVocê está em cooldown durante "
							+ DateUtils.formatDifference(CooldownAPI.getTimeLeft(p.getUniqueId(), getName())));
					return;
				} else {
					if (used.get(p.getName()) == null)
						used.put(p.getName(), Integer.valueOf(0));

					new BukkitRunnable() {
						Block b = cima;

						public void run() {
							if (GladiatorKit.gladiatorBlocks.contains(b)) {
								cancel();
								return;
							}
							if (b.getType() != Material.AIR) {
								b.setType(Material.AIR);
								b = b.getRelative(BlockFace.UP);
							} else
								cancel();
						}
					}.runTaskTimer(Main.getPlugin(), 20, 1);

					new BukkitRunnable() {
						Block b = baixo;

						public void run() {
							if (GladiatorKit.gladiatorBlocks.contains(b)) {
								cancel();
								return;
							}
							if (b.getType() != Material.AIR && b.getType() != Material.BEDROCK) {
								b.setType(Material.AIR);
								b = b.getRelative(BlockFace.DOWN);
							} else
								cancel();
						}
					}.runTaskTimer(Main.getPlugin(), 20, 1);

					used.put(p.getName(), used.get(p.getName()) + 1);
					if (used.get(p.getName()) >= 6) {
						CooldownAPI c = new CooldownAPI(p.getUniqueId(), "Jackhammer", 60);
						c.start();
						used.put(p.getName(), Integer.valueOf(0));
					}

				}
			}
		}
	}
}
