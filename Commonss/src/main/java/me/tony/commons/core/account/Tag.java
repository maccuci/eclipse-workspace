package me.tony.commons.core.account;

import lombok.Getter;

@Getter
public enum Tag {
	
	DEVELOPER(Group.DEVELOPER.getTag(), Group.DEVELOPER, true),
	ADMIN(Group.ADMIN.getTag(), Group.ADMIN, true),
	MODPLUS(Group.MODPLUS.getTag(), Group.MODPLUS, true),
	MOD(Group.MOD.getTag(), Group.MOD, true),
	HELPER(Group.HELPER.getTag(), Group.HELPER, true),
	MVPPLUSPLUS(Group.MVPPLUSPLUS.getTag(), Group.MVPPLUSPLUS, true),
	MVPPLUS(Group.MVPPLUS.getTag(), Group.MVPPLUS, true),
	MVP(Group.MVP.getTag(), Group.MVP, true),
	VIPPLUS(Group.VIPPLUS.getTag(), Group.VIPPLUS, true),
	VIP(Group.VIP.getTag(), Group.VIP, true),
	MEMBER(Group.MEMBER.getTag(), Group.MEMBER, false);
	
	private String tag;
	private Group group;
	private boolean exclusive;
	
	private Tag(String tag, Group group, boolean exclusive) {
		this.tag = tag;
		this.group = group;
		this.exclusive = exclusive;
	}
}
