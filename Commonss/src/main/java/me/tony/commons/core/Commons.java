package me.tony.commons.core;

import lombok.Getter;
import me.tony.commons.core.managements.TagManagement;

public class Commons {
	
	@Getter
	private static TagManagement tagManagement = new TagManagement();

}
