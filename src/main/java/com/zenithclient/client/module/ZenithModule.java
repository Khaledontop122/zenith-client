package com.zenithclient.client.module;

import com.zenithclient.client.safety.SafetyLock;

public final class ZenithModule {
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;

    public ZenithModule(String name, String description, ModuleCategory category, boolean enabledByDefault) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = enabledByDefault;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public ModuleCategory category() {
        return category;
    }

    public boolean enabled() {
        return enabled;
    }

    public boolean safetyLocked() {
        return category == ModuleCategory.CHEATS && SafetyLock.isSafetyLockEngaged();
    }

    public boolean canToggle() {
        return !safetyLocked();
    }

    public void toggle() {
        if (canToggle()) {
            enabled = !enabled;
        }
    }
}
