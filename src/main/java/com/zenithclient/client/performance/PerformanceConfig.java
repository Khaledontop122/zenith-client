package com.zenithclient.client.performance;

import com.zenithclient.client.module.ModuleRegistry;

import java.util.concurrent.atomic.AtomicInteger;

public final class PerformanceConfig {
    private static final AtomicInteger PARTICLE_COUNTER = new AtomicInteger();

    private PerformanceConfig() {
    }

    public static boolean shouldThrottleParticles() {
        return ModuleRegistry.isEnabled(ModuleRegistry.FPS_BOOST)
                || ModuleRegistry.isEnabled(ModuleRegistry.LOW_PARTICLE_MODE);
    }

    public static boolean shouldSkipParticleSpawn() {
        if (!shouldThrottleParticles()) {
            return false;
        }

        return PARTICLE_COUNTER.incrementAndGet() % 3 != 0;
    }

    public static boolean shouldReduceWeatherEffects() {
        return ModuleRegistry.isEnabled(ModuleRegistry.FPS_BOOST)
                || ModuleRegistry.isEnabled(ModuleRegistry.QUIET_WEATHER);
    }
}
