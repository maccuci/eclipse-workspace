package com.fuzion.core;

import lombok.Getter;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import net.minecraft.util.com.google.gson.JsonParser;

public class FuzionAPI {
	
	public static final String STORE = "loja.fuzion-network.com";
	public static final String ADDRESS = "fuzion-network.com";
	
    public static String getBungeeChannel() {
        return "Commons";
    }
    
    @Getter
    private static final Gson gson = new Gson();
    @Getter
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    @Getter
    private static final JsonParser parser = new JsonParser();

}
