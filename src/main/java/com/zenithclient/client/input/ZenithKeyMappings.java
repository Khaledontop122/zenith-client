package com.zenithclient.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class ZenithKeyMappings {
    public static final KeyMapping OPEN_CLICK_GUI = new KeyMapping(
            "key.zenithclient.open_gui",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "key.categories.zenithclient"
    );

    private ZenithKeyMappings() {
    }
}
