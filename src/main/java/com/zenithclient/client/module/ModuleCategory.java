package com.zenithclient.client.module;

public enum ModuleCategory {
    UTILITY("Utility"),
    RENDER("Render"),
    CHEATS("Cheats");

    private final String displayName;

    ModuleCategory(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
