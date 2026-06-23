package com.zenithclient.client.hud;

import com.zenithclient.ZenithClient;
import com.zenithclient.client.module.ModuleRegistry;
import com.zenithclient.client.safety.SafetyLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ZenithClient.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ZenithHudOverlay {
    private static final int BACKGROUND = 0xB0101218;
    private static final int ACCENT = 0xFF6EE7B7;
    private static final int TEXT = 0xFFE5E7EB;
    private static final int MUTED = 0xFF9CA3AF;
    private static final int WARNING = 0xFFF87171;

    private ZenithHudOverlay() {
    }

    @SubscribeEvent
    public static void render(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!ModuleRegistry.isEnabled(ModuleRegistry.MINIMAL_HUD) || minecraft.options.renderDebug || minecraft.player == null) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        Font font = minecraft.font;
        List<String> lines = hudLines(minecraft);
        int width = lines.stream().mapToInt(font::width).max().orElse(80) + 18;
        int height = lines.size() * 11 + 12;

        graphics.fill(8, 8, 8 + width, 8 + height, BACKGROUND);
        graphics.fill(8, 8, 11, 8 + height, SafetyLock.isSafetyLockEngaged() ? WARNING : ACCENT);

        int y = 14;
        for (String line : lines) {
            int color = line.startsWith("Safety") && SafetyLock.isSafetyLockEngaged() ? WARNING : TEXT;
            graphics.drawString(font, line, 17, y, color, false);
            y += 11;
        }
    }

    private static List<String> hudLines(Minecraft minecraft) {
        List<String> lines = new ArrayList<>();
        lines.add("Zenith Client");
        lines.add("FPS " + Minecraft.getFps());
        lines.add("Ping " + ping(minecraft));
        lines.add("FPS Boost " + state(ModuleRegistry.FPS_BOOST));
        lines.add("Particles " + state(ModuleRegistry.LOW_PARTICLE_MODE));
        lines.add("Safety " + (SafetyLock.isSafetyLockEngaged() ? "Locked" : "Open"));
        return lines;
    }

    private static String ping(Minecraft minecraft) {
        if (minecraft.getConnection() == null || minecraft.player == null) {
            return "--";
        }

        PlayerInfo info = minecraft.getConnection().getPlayerInfo(minecraft.player.getUUID());
        return info == null ? "--" : info.getLatency() + "ms";
    }

    private static String state(String moduleName) {
        return ModuleRegistry.isEnabled(moduleName) ? "On" : "Off";
    }
}
