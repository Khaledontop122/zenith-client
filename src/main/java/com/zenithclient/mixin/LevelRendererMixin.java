package com.zenithclient.mixin;

import com.zenithclient.client.performance.PerformanceConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Inject(method = "tickRain", at = @At("HEAD"), cancellable = true)
    private void zenith$reduceWeatherParticles(Camera camera, CallbackInfo callbackInfo) {
        if (PerformanceConfig.shouldReduceWeatherEffects()) {
            callbackInfo.cancel();
        }
    }
}
