package com.fuzion.core;

import com.fuzion.core.bukkit.player.account.AccountManager;

import lombok.Getter;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.JsonArray;

public class FuzionAPI {
	
	public static final String ADDRESS = "fuzion-network.com";
	public static final String FULL_ADDRESS = "www." + ADDRESS;
	
	public static final Gson gson = new Gson();
	public static final JsonArray json = new JsonArray();
	
	@Getter
	private static final AccountManager accountManager = new AccountManager();

}
