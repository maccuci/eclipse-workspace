package com.fuzion.core.api.cooldown;

import java.util.HashMap;
import java.util.UUID;

public class CooldownAPI {
	
	private static HashMap<String, CooldownAPI> cooldowns = new HashMap<>();
	private long start;
	private final int timeInSeconds;
	private final UUID id;
	private final String cooldownName;
	
	public CooldownAPI(UUID id, String cooldownName, int timeInSeconds) {
		this.id = id;
		this.cooldownName = cooldownName;
		this.timeInSeconds = timeInSeconds;
	}

	public static boolean isInCooldown(UUID id, String cooldownName) {
		if (getTimeLeft(id, cooldownName) >= 1) {
			return true;
		}
		stop(id, cooldownName);
		return false;
	}

	private static void stop(UUID id, String cooldownName) {
		cooldowns.remove(id + cooldownName);
	}

	private static CooldownAPI getCooldown(UUID id, String cooldownName) {
		return cooldowns.get(id.toString() + cooldownName);
	}

	public static CooldownAPI getCooldowns(UUID id, String cooldownName) {
		return cooldowns.get(id.toString() + cooldownName);
	}

	public static int getTimeLeft(UUID id, String cooldownName) {
		CooldownAPI cooldown = getCooldown(id, cooldownName);
		int f = -1;
		if (cooldown != null) {
			long now = System.currentTimeMillis();
			long cooldownTime = cooldown.start;
			int totalTime = cooldown.timeInSeconds;
			int r = (int) (now - cooldownTime) / 1000;
			f = (r - totalTime) * -1;
		}
		return f;
	}

	public void start() {
		this.start = System.currentTimeMillis();
		cooldowns.put(this.id.toString() + this.cooldownName, this);
	}
}
