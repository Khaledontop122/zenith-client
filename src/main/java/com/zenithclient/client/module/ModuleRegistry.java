package com.zenithclient.client.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class ModuleRegistry {
    public static final String MINIMAL_HUD = "Minimal HUD";
    public static final String FPS_BOOST = "FPS Boost";
    public static final String LOW_PARTICLE_MODE = "Low Particle Mode";
    public static final String QUIET_WEATHER = "Quiet Weather";

    private static final List<ZenithModule> MODULES = createModules();

    private ModuleRegistry() {
    }

    public static List<ZenithModule> all() {
        return Collections.unmodifiableList(MODULES);
    }

    public static List<ZenithModule> byCategory(ModuleCategory category) {
        return MODULES.stream()
                .filter(module -> module.category() == category)
                .toList();
    }

    public static boolean isEnabled(String name) {
        return find(name).map(ZenithModule::enabled).orElse(false);
    }

    public static Optional<ZenithModule> find(String name) {
        return MODULES.stream()
                .filter(module -> module.name().equals(name))
                .findFirst();
    }

    private static List<ZenithModule> createModules() {
        List<ZenithModule> modules = new ArrayList<>();
        modules.add(new ZenithModule(MINIMAL_HUD, "Shows FPS, ping, and active utility states.", ModuleCategory.UTILITY, true));
        modules.add(new ZenithModule(FPS_BOOST, "Enables conservative rendering stutter reducers.", ModuleCategory.UTILITY, false));
        modules.add(new ZenithModule(LOW_PARTICLE_MODE, "Throttles nonessential client particles.", ModuleCategory.RENDER, false));
        modules.add(new ZenithModule(QUIET_WEATHER, "Reduces weather particle work on the client.", ModuleCategory.RENDER, false));
        modules.add(new ZenithModule("Cheat Slot A", "Placeholder entry blocked by Safety Lock on Hypixel.", ModuleCategory.CHEATS, false));
        modules.add(new ZenithModule("Cheat Slot B", "Placeholder entry blocked by Safety Lock on Hypixel.", ModuleCategory.CHEATS, false));
        modules.add(new ZenithModule("Cheat Slot C", "Placeholder entry blocked by Safety Lock on Hypixel.", ModuleCategory.CHEATS, false));
        return modules;
    }
}
