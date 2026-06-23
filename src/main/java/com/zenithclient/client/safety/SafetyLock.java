package com.zenithclient.client.safety;

import com.zenithclient.ZenithClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = ZenithClient.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SafetyLock {
    public static volatile boolean isSafetyLockEngaged = false;

    private static boolean announcedForCurrentServer = false;

    private SafetyLock() {
    }

    public static boolean isSafetyLockEngaged() {
        return isSafetyLockEngaged;
    }

    @SubscribeEvent
    public static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        evaluateCurrentServer();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            evaluateCurrentServer();
        }
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        isSafetyLockEngaged = false;
        announcedForCurrentServer = false;
    }

    private static void evaluateCurrentServer() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        ServerData serverData = minecraft.getCurrentServer();
        String address = serverData == null || serverData.ip == null
                ? ""
                : serverData.ip.toLowerCase(Locale.ROOT);

        boolean hypixelDetected = address.contains("hypixel.net");
        isSafetyLockEngaged = hypixelDetected;

        if (hypixelDetected && !announcedForCurrentServer) {
            announcedForCurrentServer = true;
            minecraft.gui.getChat().addMessage(Component.literal("[Zenith Client] Hypixel detected. Cheats tab disabled by Safety Lock."));
            ZenithClient.LOGGER.info("Hypixel detected. Zenith Client Safety Lock is engaged.");
        }
    }
}
