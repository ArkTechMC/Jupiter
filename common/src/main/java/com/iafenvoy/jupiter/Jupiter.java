package com.iafenvoy.jupiter;

import com.iafenvoy.jupiter.network.ClientConfigNetwork;
import com.iafenvoy.jupiter.network.ServerConfigNetwork;
import com.iafenvoy.jupiter.network.ServerNetworkHelper;
import com.iafenvoy.jupiter.network.payload.ConfigErrorPayload;
import com.iafenvoy.jupiter.network.payload.ConfigRequestPayload;
import com.iafenvoy.jupiter.network.payload.ConfigSyncPayload;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class Jupiter {
    public static final String MOD_ID = "jupiter";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        ServerNetworkHelper.registerPayloadType(ConfigSyncPayload.ID, ConfigSyncPayload.CODEC);
        ServerNetworkHelper.registerPayloadType(ConfigRequestPayload.ID, ConfigRequestPayload.CODEC);
        ServerNetworkHelper.registerPayloadType(ConfigErrorPayload.ID, ConfigErrorPayload.CODEC);
    }

    public static void process() {
        ServerConfigNetwork.init();
    }

    public static void processClient() {
        ClientConfigNetwork.init();
    }
}
