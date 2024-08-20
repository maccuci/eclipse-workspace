package me.feenks.hg.game.stage;

public enum GameStage {
	
	NONE(),
	WAITING(CounterType.COUNTDOWN, 300),
	INVINCIBILITY(CounterType.COUNTDOWN, 120),
	GAME(CounterType.COUNT_UP, 120);
	
	private int defaultTimer;
	private CounterType defaultType;
	
	private GameStage() {
		this(CounterType.STOP, 0);
	}

	private GameStage(CounterType type) {
		this(type, 0);
	}

	private GameStage(CounterType type, int timer) {
		defaultType = type;
		defaultTimer = timer;
	}

	public CounterType getDefaultType() {
		return defaultType;
	}

	public int getDefaultTimer() {
		return defaultTimer;
	}

	public static boolean isPregame(GameStage stage) {
		return stage == GameStage.WAITING;
	}

	public static boolean isInvincibility(GameStage stage) {
		return stage == GameStage.INVINCIBILITY;
	}
}
