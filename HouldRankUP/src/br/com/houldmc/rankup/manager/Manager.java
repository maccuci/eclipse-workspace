package br.com.houldmc.rankup.manager;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.api.cooldown.CooldownAPI;
import br.com.houldmc.rankup.api.enchantment.EnchantmentAPI;
import br.com.houldmc.rankup.api.enchantment.enchantments.RemoveBedrockCustomEnchantment;
import br.com.houldmc.rankup.api.menu.MenuListener;
import br.com.houldmc.rankup.api.stack.EntityStackerTask;
import br.com.houldmc.rankup.api.stack.MobStackListener;
import br.com.houldmc.rankup.manager.command.CommandLoader;
import br.com.houldmc.rankup.manager.economy.EconomyManager;
import br.com.houldmc.rankup.manager.kit.KitManager;
import br.com.houldmc.rankup.manager.mine.MineManager;
import br.com.houldmc.rankup.manager.mysql.MySQL;
import br.com.houldmc.rankup.manager.rank.RankManager;
import br.com.houldmc.rankup.manager.scoreboard.ScoreboardManager;
import br.com.houldmc.rankup.player.listener.AccountListener;
import br.com.houldmc.rankup.player.listener.PlayerHandlerListeners;
import br.com.houldmc.rankup.player.listener.WandListener;
import lombok.Getter;

@Getter
public class Manager {
	
	private Main plugin;
	private ScoreboardManager scoreboardManager;
	private RankManager rankManager;
	private MySQL mySQL;
	private EntityStackerTask entityStackerTask;
	private EconomyManager economyManager;
	
	public Manager() {
		plugin = Main.getPlugin();
		
		scoreboardManager = new ScoreboardManager();
		rankManager = new RankManager();
		mySQL = new MySQL();
		entityStackerTask = new EntityStackerTask();
		economyManager = new EconomyManager();
		
		entityStackerTask.runTaskTimer(plugin, 20L, 20L);
		
		getPlugin().getServer().getPluginManager().registerEvents(new PlayerHandlerListeners(), plugin);
		getPlugin().getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
		getPlugin().getServer().getPluginManager().registerEvents(new MobStackListener(), plugin);
		getPlugin().getServer().getPluginManager().registerEvents(new CooldownAPI(), plugin);
		getPlugin().getServer().getPluginManager().registerEvents(new AccountListener(), plugin);
		getPlugin().getServer().getPluginManager().registerEvents(new WandListener(), plugin);
		
		KitManager.loadKits();
		EconomyManager.loadItems();
		new CommandLoader().loadCommandsFromPackage("br.com.houldmc.rankup.commands");
		
		if (mySQL.openConnection()) {
			System.out.println("The primary and secondary connections have been established.");
		} else {
			System.out.println("Error the mysql connections have not been established.");
		}
		new MineManager().loadMines();
		new EnchantmentAPI().loadEnchantment(new RemoveBedrockCustomEnchantment());
		
		for (int x = -250; x < 250; x++) {
			for (int z = -250; z < 250; z++) {
				Chunk chunk = Bukkit.getWorld("world").getBlockAt(x, 64, z).getChunk();
				if (chunk.isLoaded()) {
					continue;
				}
				chunk.load(true);
			}
		}

		for (Entity ent : Bukkit.getWorld("world").getEntities()) {
			ent.remove();
		}
	}
}
