package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

import net.minecraft.server.v1_7_R4.EntityPlayer;

public class VampireKit extends Kit {
	
	public VampireKit() {
		super("Vampire", "Efeitos", new ItemStack(Material.POTION, 1, (short)16421), Group.BETA, "§7Ao matar algum jogador, tome a vida dele pra si.");
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if (event.getEntity().getKiller() != null && hasKit(event.getEntity().getKiller())) {
			Player p = event.getEntity().getKiller();
			if (event.getEntity() instanceof Creature) {
				EntityPlayer p2 = ((CraftPlayer) p).getHandle();
				double hp = p2.getHealth();
				hp += (event.getEntity() instanceof Animals ? 3 : 5);
				if (hp > 20) {
					hp = 20;
				}
				if (hasKit(p))
					p.setHealth(hp);
			}
		}
	}

	@EventHandler
	public void DeathXP(PlayerDeathEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getEntity().getKiller() instanceof Player))) {
			Player p = e.getEntity();
			Player k = p.getKiller();
			if (hasKit(k))
				k.getInventory().addItem(new ItemStack[] { new ItemStack(Material.POTION, 1, (short) 16421) });
		}
	}

}
