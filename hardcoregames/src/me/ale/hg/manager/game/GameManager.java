package me.ale.hg.manager.game;

import lombok.Getter;
import lombok.Setter;
import me.ale.hg.manager.timer.InvincibilityTimer;
import me.ale.hg.manager.timer.PreGameTimer;

public class GameManager {
	
	@Getter
	@Setter
	private static GameType type;
	
	public GameManager() {
		
	}
	
	public static void startTime(GameType type) {
		switch (type) {
		case WAITING:
			PreGameTimer.startCountdown();
			break;
			
		case INVINCIBILITY:
			InvincibilityTimer.startCountdown();
			break;

		default:
			break;
		}
	}
	public static boolean isPreGame() {
		return type == GameType.WAITING;
	}
	
	public static boolean isInvincibility() {
		return type == GameType.INVINCIBILITY;
	}
	
	public static boolean isGame() {
		return type == GameType.GAMETIME;
	}
}
