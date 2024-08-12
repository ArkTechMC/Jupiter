package com.iafenvoy.jupiter;

import com.iafenvoy.jupiter.malilib.config.ConfigManager;
import com.iafenvoy.jupiter.network.ClientConfigNetwork;
import com.iafenvoy.jupiter.network.ServerConfigNetwork;
import com.iafenvoy.jupiter.test.TestConfig;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class Jupiter {
    public static final String MOD_ID = "jupiter";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
    }

    public static void process() {
        ConfigManager.getInstance().registerConfigHandler(TestConfig.INSTANCE);
        ServerConfigNetwork.init();
        ServerConfigManager.registerServerConfig(TestConfig.INSTANCE, ServerConfigManager.PermissionChecker.IS_OPERATOR);
    }

    public static void processClient() {
        ClientConfigNetwork.init();
    }
}
