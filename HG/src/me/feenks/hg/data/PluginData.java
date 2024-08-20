package me.feenks.hg.data;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import lombok.Getter;
import me.feenks.hg.HardcoreGames;
import me.feenks.hg.game.ModeMain;
import me.feenks.hg.manager.KitManager;
import me.feenks.hg.utils.event.UpdateScheduler;

public class PluginData {
	
	@Getter
	private HardcoreGames hardcoreGames;
	
	@Getter
	private UpdateScheduler updateScheduler;
	
	@Getter private ModeMain modeMain;
	@Getter private KitManager kitManager;
	
	public PluginData(HardcoreGames hardcoreGames) {
		this.hardcoreGames = hardcoreGames;
		
		updateScheduler = new UpdateScheduler();
		modeMain = new ModeMain(hardcoreGames);
		kitManager = new KitManager();
	}
	
	public boolean initialize() {
		if(!hardcoreGames.isEnabled())
			return false;
		
		updateScheduler.run();
		kitManager.initializateKits("me.feenks.hg.game.kits");
		
		HardcoreGames.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(HardcoreGames.getPlugin(), "BungeeCord");
		HardcoreGames.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(HardcoreGames.getPlugin(), "BungeeCord", new PluginMessageListener() {
			public void onPluginMessageReceived(String channel, Player dontMatter, byte[] bytes) {

			}
		});

		return true;
	}
}
