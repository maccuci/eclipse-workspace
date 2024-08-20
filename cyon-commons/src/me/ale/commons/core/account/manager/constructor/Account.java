package me.ale.commons.core.account.manager.constructor;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Account {
	
	@Getter
	@NonNull
	private String className;
	
	@Getter
	@NonNull
	private AccountType accountType;

}
