package com.hother.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import lombok.Getter;

public class HotherAPI {
	
    public static String getBungeeChannel() {
        return "HotherCore";
    }
	
	@Getter
    private static final Gson gson = new Gson();
    @Getter
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    @Getter
    private static final JsonParser parser = new JsonParser();

}
