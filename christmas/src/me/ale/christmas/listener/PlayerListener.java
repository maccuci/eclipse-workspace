package me.ale.christmas.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.ale.christmas.menu.GiftsMenu;
//import me.ale.christmas.util.DateUtils;
import me.ale.christmas.util.ItemBuilder;
import me.ale.christmas.util.MessageUtils;

public class PlayerListener implements Listener {
	
	private MessageUtils message = new MessageUtils();
	private ItemBuilder builder = new ItemBuilder();
	
	@EventHandler
	public void ongbfdgfsd(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String name = player.getItemInHand().getItemMeta().getDisplayName();
		
		if(player.getItemInHand().getType() == Material.SKULL_ITEM || name == message.translateColor("Item_Name")) {
			event.setCancelled(true);
			new GiftsMenu().open(player);
			return;
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		//if(DateUtils.isChristmas()) {
			player.sendMessage(message.translateColor("Join_Message"));
			player.getInventory().setItem(4, builder.type(Material.SKULL_ITEM).durability(3).name(message.translateColor("Item_Name")).skinURL("http://textures.minecraft.net/texture/71e82a346f0c5cd2bd2a18bd2dbb5eb43e1fd2581d72b198e1b79d17708e0e91").build());
		//}
	}
}
