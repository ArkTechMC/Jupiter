package com.iafenvoy.jupiter;

import com.iafenvoy.jupiter.network.ClientConfigNetwork;
import com.iafenvoy.jupiter.network.ServerConfigNetwork;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class Jupiter {
    public static final String MOD_ID = "jupiter";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void process() {
        ServerConfigNetwork.init();
    }

    public static void processClient() {
        ClientConfigNetwork.init();
    }
}
