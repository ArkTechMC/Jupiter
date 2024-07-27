package com.iafenvoy.jupiter;

import com.mojang.logging.LogUtils;
import fi.dy.masa.malilib.config.ConfigManager;
import org.slf4j.Logger;

public final class Jupiter {
    public static final String MOD_ID = "jupiter";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {

    }

    public static void process() {
        ((ConfigManager) ConfigManager.getInstance()).loadAllConfigs();
    }

    public static void processClient() {
    }
}
