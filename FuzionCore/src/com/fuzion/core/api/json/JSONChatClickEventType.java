package com.fuzion.core.api.json;

public enum JSONChatClickEventType {
	
    RUN_COMMAND("run_command"),
    SUGGEST_COMMAND("suggest_command"),
    OPEN_URL("open_url");
	
    private final String type;

    JSONChatClickEventType(String type) {
        this.type = type;
    }

    public String getTypeString() {
        return type;
    }
}
