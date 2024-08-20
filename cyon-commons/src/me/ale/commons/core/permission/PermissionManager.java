package me.ale.commons.core.permission;

import me.ale.commons.core.permission.injector.PermissionMatcher;
import me.ale.commons.core.permission.injector.RegExpMatcher;
import me.ale.commons.core.permission.injector.regexperm.RegexPermissions;
import me.ale.commons.core.permission.listener.PermissionListener;

public class PermissionManager {

	private RegexPermissions regexPerms;
	protected PermissionMatcher matcher = new RegExpMatcher();
	protected PermissionListener superms;

	public void run() {
		regexPerms = new RegexPermissions(this);
	}

	public void stop() {
		if (this.regexPerms != null) {
			this.regexPerms.onDisable();
			this.regexPerms = null;
		}
		if (this.superms != null) {
			this.superms.onDisable();
			this.superms = null;
		}
	}

	public RegexPermissions getRegexPerms() {
		return regexPerms;
	}

	public PermissionMatcher getPermissionMatcher() {
		return this.matcher;
	}
}
