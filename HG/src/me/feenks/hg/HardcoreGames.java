package me.feenks.hg;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import me.feenks.hg.data.PluginData;
import me.feenks.hg.game.event.game.GameStageChangeEvent;
import me.feenks.hg.game.stage.CounterType;
import me.feenks.hg.game.stage.GameStage;
import me.feenks.hg.utils.Utils.MasterLogger;

public class HardcoreGames extends JavaPlugin {
	
	public static HardcoreGames getPlugin() {
		return HardcoreGames.getPlugin(HardcoreGames.class);
	}
	
	@Getter
	private PluginData pluginData = new PluginData(this);
	
	@Getter private GameStage gameStage = GameStage.WAITING;
	@Getter private CounterType timerType = CounterType.STOP;
	@Getter private int timer;
	@Setter private int minimumPlayers = 5;
	@Setter private int totalPlayers;
	
	@Getter
	private MasterLogger masterLogger = new MasterLogger(getLogger(), "", true);
	
	@Override
	public void onLoad() {
		pluginData.getModeMain().onLoad();
	}
	
	@Override
	public void onEnable() {
		
		if(pluginData.initialize()) {
			pluginData.getModeMain().onEnable();
			masterLogger.debug("Plugin was actived.");
		} else {
			masterLogger.error("Plugin wasn't actived, PluginData error.");
		}
	}
	
	public void setStage(GameStage gameStage) {
		getServer().getPluginManager().callEvent(new GameStageChangeEvent(this.gameStage, gameStage));
		this.gameStage = gameStage;
		setTimer(gameStage.getDefaultTimer());
	}
	
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	public void setTimerType(CounterType timerType) {
		this.timerType = timerType;
	}
	
	public void count() {
		switch (timerType) {
		case COUNTDOWN:
			setTimer(timer - 1);
			break;
		case COUNT_UP:
			setTimer(timer + 1);
			break;
		default:
			break;

		}
	}
	
	public int playersLeft() {
		int i = 0;
		for (Player p : getServer().getOnlinePlayers()) {
			if(!p.isOnline()) {
				continue;
			}
			i++;
		}
		return i;
	}
}
