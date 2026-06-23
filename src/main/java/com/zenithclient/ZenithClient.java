package com.zenithclient;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ZenithClient.MOD_ID)
public final class ZenithClient {
    public static final String MOD_ID = "zenithclient";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ZenithClient() {
        LOGGER.info("Zenith Client initialized.");
    }
}
