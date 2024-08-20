package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.commons.core.account.manager.StatsManager;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;

public class SamuraiKit extends Kit {
	
	static final ItemBuilder builder = new ItemBuilder();
	
	public SamuraiKit() {
		super("Samurai", Material.IRON_SWORD, null, KitRarity.MYSTIC, Rank.SIMPLE, "�7Voc� � um samurai que est� em busca da espada perfeita!", "�7Para consigui-la voc� tem que matar todos os", "�7oponentes que entrarem na sua jornada!");
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(!(event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player))
			return;
		
		if(event.getEntity().getType() == EntityType.PLAYER && event.getEntity().getKiller().getType() == EntityType.PLAYER) {
			Player killer = (Player)event.getEntity().getKiller();
			
			if(new StatsManager().get(killer.getUniqueId(), "streak") < 5) {
				killer.sendMessage("�cSe acalme, voc� est� quase conseguindo come�ar a sua jornada.");
				return;
			}
			
			if(killer.getItemInHand().getType() == Material.STONE_SWORD) {
				killer.setItemInHand(builder.type(Material.IRON_SWORD).build());
				killer.sendMessage("�aParab�ns samurai voc� elevou a sua espada para o n�vel II. Continue assim, que logo a sua jornada acabar�.");
			} else if(killer.getItemInHand().getType() == Material.IRON_SWORD) {
				killer.setItemInHand(builder.type(Material.IRON_SWORD).enchantment(Enchantment.DAMAGE_ALL, 1).build());
				killer.sendMessage("�aParab�ns samurai voc� elevou a sua espada para o n�vel III. Continue assim, que logo a sua jornada acabar�.");
			} else if(killer.getItemInHand().getType() == Material.IRON_SWORD || killer.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL) && killer.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 1) {
				killer.setItemInHand(builder.type(Material.IRON_SWORD).enchantment(Enchantment.DAMAGE_ALL, 2).build());
				killer.sendMessage("�aParab�ns samurai voc� elevou a sua espada para o n�vel IV. Continue assim, que logo a sua jornada acabar�.");
			} else if(killer.getItemInHand().getType() == Material.IRON_SWORD || killer.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL) && killer.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 2) {
				killer.setItemInHand(builder.type(Material.IRON_SWORD).enchantment(Enchantment.DAMAGE_ALL, 3).build());
				killer.sendMessage("�aParab�ns samurai voc� elevou a sua espada para o n�vel V. Continue assim, que logo a sua jornada acabar�.");
			} else if(killer.getItemInHand().getType() == Material.IRON_SWORD || killer.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL) && killer.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 3) {
				killer.setItemInHand(builder.type(Material.DIAMOND_SWORD).build());
				killer.sendMessage("�aParab�ns samurai voc� elevou a sua espada para o n�vel VI. Continue assim, que logo a sua jornada acabar�.");
			} else if(killer.getItemInHand().getType() == Material.DIAMOND_SWORD) {
				killer.setItemInHand(builder.type(Material.DIAMOND_SWORD).enchantment(Enchantment.DAMAGE_ALL, 1).build());
				killer.sendMessage("�aParab�ns samurai voc� elevou a sua espada para o n�vel VII. Continue assim, que logo a sua jornada acabar�.");
			} else if(killer.getItemInHand().getType() == Material.DIAMOND_SWORD || killer.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL) && killer.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 1) {
				killer.setItemInHand(builder.type(Material.DIAMOND_SWORD).enchantment(Enchantment.DAMAGE_ALL, 2).build());
				killer.sendMessage("�aParab�ns samurai voc� elevou a sua espada para o n�vel VIII. Continue assim, que logo a sua jornada acabar�.");
			} else if(killer.getItemInHand().getType() == Material.DIAMOND_SWORD || killer.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL) && killer.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 2) {
				killer.setItemInHand(builder.type(Material.DIAMOND_SWORD).enchantment(Enchantment.DAMAGE_ALL, 3).build());
				killer.sendMessage("�aParab�ns samurai voc� elevou a sua espada para o n�vel IX. Continue assim, que logo a sua jornada acabar�.");
			} else if(killer.getItemInHand().getType() == Material.DIAMOND_SWORD || killer.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL) && killer.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 3) {
				killer.setItemInHand(builder.type(Material.DIAMOND_SWORD).enchantment(Enchantment.DAMAGE_ALL, 4).build());
				killer.sendMessage("�aParab�ns samurai voc� terminou a sua jornada em busca da espada perfeita.");
			}
		}
	}
}
