package com.fuzion.core.master.permission;

import com.fuzion.core.master.permission.injector.PermissionMatcher;
import com.fuzion.core.master.permission.injector.RegExpMatcher;
import com.fuzion.core.master.permission.injector.regexperm.RegexPermissions;
import com.fuzion.core.master.permission.listener.PermissionListener;

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
