package com.zenithclient.client;

import com.zenithclient.ZenithClient;
import com.zenithclient.client.gui.ZenithClickGuiScreen;
import com.zenithclient.client.input.ZenithKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ZenithClient.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ZenithClientForgeEvents {
    private ZenithClientForgeEvents() {
    }

    @SubscribeEvent
    public static void openClickGui(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        while (ZenithKeyMappings.OPEN_CLICK_GUI.consumeClick()) {
            minecraft.setScreen(new ZenithClickGuiScreen());
        }
    }
}
