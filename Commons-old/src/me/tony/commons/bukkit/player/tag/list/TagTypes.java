package me.tony.commons.bukkit.player.tag.list;

public enum TagTypes {
	
	OWNER("Dono", "§4§lDONO§f ", "§4§l"),
	ADMINISTRATOR("Admin", "§c§lADMIN§f ", "§c§l"),
	MODERATOR_PLUS("ModPlus", "§5§lMOD+§f ", "§5§l"),
	MODERATOR("Mod", "§5§lMOD§f ", "§5§l"),
	TRIAL("Trial", "§5§lTRIAL§f ", "§5§l"),
	YOUTUBERPLUS("YoutuberPlus", "§3§lYT+§f ", "§3§l"),
	HELPER("Ajudante", "§9§lAJUDANTE§f ", "§9§l"),
	BUILDER("Builder", "§e§lBUILDER§f ", "§e§l"),
	YOUTUBER("Youtuber", "§b§lYT§f ", "§b§l"),
	BETA("Beta", "§1§lBETA§f ", "§1§l"),
	OLYMPUS("Olympus", "§f§lOLYMPUS§f ", "§f§l"),
	ARES("Ares", "§6§lARES§f ", "§6§l"),
	HADES("Hades", "§c§lHADES§f ", "§c§l"),
	VIP("Vip", "§a§lVIP§f ", "§a§l"),
	NORMAL("Normal", "§f ", "§7§l");
	
	private String tagName, prefix, color;
	
	private TagTypes(String tagName, String prefix, String color) {
		this.tagName = tagName;
		this.prefix = prefix;
		this.color = color;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getColor() {
		return color;
	}
	
	public static TagTypes getTag(String name) {
		for(TagTypes tag : values()) {
			if(tag.getTagName() == name)
				return tag;
		}
		return null;
	}
}
