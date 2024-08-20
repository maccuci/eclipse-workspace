package br.com.houldmc.rankup.api.stack;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class MobStackAPI {
	
	public static String CUSTOM_NAME_FORMAT = "\u00A7ex%1$d";
	public static int INVALID_STACK = -1;
	private static final Set<EntityType> mobTypes = new HashSet<EntityType>();

	/*
	 * Stack/unstack methods
	 */
	public static boolean attemptUnstackOne(LivingEntity livingEntity) {
		ChatColor stackNumberColor = ChatColor.WHITE;

		String displayName = livingEntity.getCustomName();
		int stackAmt = parseAmount(displayName);

		// Kill this mob
		livingEntity.setHealth(0);

		if (stackAmt <= 1) {
			return false;
		}

		// Recreate the stack with one less mob
		stackAmt--;
		String newDisplayName = String.format(stackNumberColor + CUSTOM_NAME_FORMAT, stackAmt);

		LivingEntity dupEntity = (LivingEntity) livingEntity.getWorld().spawnEntity(livingEntity.getLocation(), livingEntity.getType());
		dupEntity.setCustomName(newDisplayName);
		dupEntity.setCustomNameVisible(true);

		return true;
	}

	public static boolean stack(LivingEntity target, LivingEntity stackee) {
		if (target.getType() != stackee.getType()) {
			return false; // The entities must be of the same type.
		}
		ChatColor stackNumberColor = ChatColor.WHITE;

		String displayName = target.getCustomName();
		int alreadyStacked = parseAmount(displayName);
		int stackDelta = 1;

		// Check if the stackee is already a stack
		if (isStacked(stackee)) {
			stackDelta = parseAmount(stackee.getCustomName());
		}

		stackee.remove();

		if (alreadyStacked == INVALID_STACK) {
			// The target is NOT a stack
			String newDisplayName = String.format(stackNumberColor + CUSTOM_NAME_FORMAT, 1 + stackDelta);
			target.setCustomName(newDisplayName);
			target.setCustomNameVisible(true);
		} else {
			// The target is already a stack
			String newDisplayName = String.format(stackNumberColor + CUSTOM_NAME_FORMAT, alreadyStacked + stackDelta);
			target.setCustomName(newDisplayName);
		}
		return true;
	}

	/*
	 * "Helper" methods
	 */
	@SuppressWarnings("unlikely-arg-type")
	public static int parseAmount(String displayName) {
		if (displayName == null) {
			return INVALID_STACK; // No display name, therefor not a stack.
		}
				
		// Fetch the stack number color
		ChatColor stackNumberColor = ChatColor.WHITE;

		String nameColor = ChatColor.getLastColors(displayName);
		if (nameColor.equals(ChatColor.COLOR_CHAR + stackNumberColor.getChar())) {
			return INVALID_STACK; // Not a valid stack.
		}
				
		String cleanDisplayName = displayName.replace("\u00A7ex", "");
		String cleanDisplayName2 = cleanDisplayName.replace("§f", "");

		if (cleanDisplayName2.matches("[0-9]+") == false){
			return INVALID_STACK;
		}else if (cleanDisplayName2.length() > 4 == true){
			return INVALID_STACK;
		}else{
			return Integer.parseInt(cleanDisplayName2);
		}
	}

	public static boolean isStacked(LivingEntity entity) {
		return parseAmount(entity.getCustomName()) != INVALID_STACK;
	}
	
	public static Set<EntityType> getMobsType() {
		for(EntityType entityType : EntityType.values()) {
			mobTypes.add(entityType);
		}
		return mobTypes;
	}

}
