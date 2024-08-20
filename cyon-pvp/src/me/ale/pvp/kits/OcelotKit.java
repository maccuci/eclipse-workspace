package me.ale.pvp.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.disiguise.DisiguiseAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class OcelotKit extends Kit {

	private List<Player> transform = new ArrayList<>();
	
	public OcelotKit() {
		super("Ocelot", Material.MONSTER_EGG, new ItemStack(Material.PAPER), KitRarity.RARE, Rank.SIMPLE, "§7Clique no seu item para se transformar", "§7em um guepardo, mas atenção", "§7se alguém te hitar você volta a ser um player.");
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (!hasKit(player))
			return;
		Action a = event.getAction();
		ItemStack item = player.getItemInHand();
		if (!a.name().contains("RIGHT") && !a.name().contains("LEFT"))
			return;
		if (item == null)
			return;
		ItemStack ITEM = getItem();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		if (a.name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.hasCooldown(player, getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}
		
		event.setCancelled(true);
		new DisiguiseAPI(player, EntityType.OCELOT, player.getName()).transformPlayerToMob();
		player.sendMessage("§aVocê se transformou em um guepardo! Se alguém te hitar você voltará a ser um player.");
		transform.add(player);
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 15L));
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.PLAYER) {
			return;
		}

		if (!hasKit((Player)event.getEntity()))
			return;

		if (event.getDamager() instanceof Player || event.getEntity() instanceof Player) {
			Player damaged = (Player)event.getEntity();
			if(transform.contains(damaged)) {
				new DisiguiseAPI(damaged, EntityType.OCELOT, damaged.getName()).removeTransform();
				transform.remove(damaged);
			}
		}
	}
}
