package com.fuzion.hg.kits;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.Kit;
import com.fuzion.hg.manager.kit.KitManager;

public class LauncherKit extends Kit {
	
	public static ArrayList<Player> nofalldamage = new ArrayList<>();
	
	public LauncherKit() {
		super("Launcher", "Torre", new ItemStack(Material.SPONGE), Group.BETA, "§7Pule na sua esponja para ganhar um boost para cima.");
		addItem(new ItemStack(Material.SPONGE, 10));
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!GameManager.FINISHED) {
			Location standBlock = player.getWorld().getBlockAt(player.getLocation().add(0.0D, -0.01D, 0.0D)).getLocation();
			if (standBlock.getBlock().getType() == Material.SPONGE) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3 * 20, 0));
				player.setVelocity(new Vector(0, 3, 0));

				if (!nofalldamage.contains(player)) {
					nofalldamage.add(player);
				}

				if (new KitManager().getKitName(player) == "Stomper") {
					nofalldamage.remove(player);
					return;
				}
			}
		}
	}
}
