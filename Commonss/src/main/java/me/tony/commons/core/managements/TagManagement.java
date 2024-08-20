package me.tony.commons.core.managements;

import me.tony.commons.core.account.Tag;

public class TagManagement {
	
	public boolean createDefaultsTags() {
		for(Tag tag : Tag.values()) {
			System.out.println("NAME: " + tag.getGroup().getName() + " TAG: " + tag.getTag() + " ID: " + tag.getGroup().getId());
		}
		return false;
	}
	
}
