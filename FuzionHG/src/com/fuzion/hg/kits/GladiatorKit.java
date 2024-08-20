package com.fuzion.hg.kits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.GladiatorFight;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.Kit;

public class GladiatorKit extends Kit {
	
	public static List<UUID> playersIn1v1 = new ArrayList<>();
	public static List<Block> gladiatorBlocks = new ArrayList<>();
	
	public GladiatorKit() {
		super("Gladiator", "Contra times", new ItemStack(Material.IRON_FENCE), Group.BETA, "§7Crie uma arena de vidro, e batalhe nas alturas.");
		addItem(new ItemStack(Material.IRON_FENCE, 1));
	}

	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity e = event.getRightClicked();
		ItemStack i = player.getItemInHand();
		if (!(e instanceof Player)) {
			return;
		}

		if (i.getType() == null) {
			return;
		}
		if (i.getType() != Material.IRON_FENCE) {
			return;
		}
		if (i.getItemMeta() == null) {
			return;
		}
		if (((Player) e).isDead()) {
			return;
		}
		if (playersIn1v1.contains(player.getUniqueId())) {
			return;
		}
		if (playersIn1v1.contains(((Player) e).getUniqueId())) {
			return;
		}

		if (!hasKit(player))
			return;

		if (!GameManager.isGame()) {
			player.sendMessage("§cVocê não pode usar isto agora.");
			return;
		}
		
		Player target = (Player)e;
		
		new GladiatorFight(player, target);

	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();

		ItemStack i = p.getItemInHand();
		if ((event.getAction() != Action.PHYSICAL) && (hasKit(p)) && (i.getType() != null) && (i.getType() == Material.IRON_FENCE)) {
			p.updateInventory();
			event.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlock(BlockDamageEvent event) {
		if (gladiatorBlocks.contains(event.getBlock())) {
			Block b = event.getBlock();
			Player p = event.getPlayer();
			p.sendBlockChange(b.getLocation(), Material.BEDROCK, (byte) 0);
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		Iterator<Block> blockIt = event.blockList().iterator();
		while (blockIt.hasNext()) {
			Block b = (Block) blockIt.next();
			if (gladiatorBlocks.contains(b)) {
				blockIt.remove();
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (gladiatorBlocks.contains(event.getBlock())) {
			event.setCancelled(true);
		}
	}

	public static boolean inGladiator(Player p) {
		return playersIn1v1.contains(p.getUniqueId());
	}

}
