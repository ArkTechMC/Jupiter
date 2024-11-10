package com.iafenvoy.jupiter.network;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.iafenvoy.jupiter.config.container.AbstractConfigContainer;
import com.iafenvoy.jupiter.network.payload.ConfigErrorPayload;
import com.iafenvoy.jupiter.network.payload.ConfigRequestPayload;
import com.iafenvoy.jupiter.network.payload.ConfigSyncPayload;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ServerConfigNetwork {
    public static void init() {
        ServerNetworkHelper.registerReceiver(ConfigRequestPayload.ID, (server, player, payload) -> {
            Identifier id = payload.id();
            Jupiter.LOGGER.info("Player {} request to open config {}", player.getName().getString(), id);
            boolean b = ServerConfigManager.checkPermission(id, server, player);
            NbtCompound compound;
            if (b) {
                AbstractConfigContainer data = ServerConfigManager.getConfig(id);
                assert data != null;
                compound = (NbtCompound) data.serializeNbt();
            } else compound = new NbtCompound();
            return () -> ServerNetworkHelper.sendToPlayer(player, new ConfigSyncPayload(id, b, compound));
        });
        ServerNetworkHelper.registerReceiver(ConfigSyncPayload.ID, (server, player, payload) -> {
            Identifier id = payload.id();
            Jupiter.LOGGER.info("Player {} request to change config {}", player.getName().getString(), id);
            NbtCompound data = payload.compound();
            return () -> {
                if (ServerConfigManager.checkPermission(id, server, player)) {
                    AbstractConfigContainer container = ServerConfigManager.getConfig(id);
                    if (container != null) {
                        Jupiter.LOGGER.info(data.toString());
                        container.deserializeNbt(data);
                        container.onConfigsChanged();
                        Jupiter.LOGGER.info("Player {} changed config {}", player.getName().getString(), id);
                    }
                } else
                    ServerNetworkHelper.sendToPlayer(player, new ConfigErrorPayload());
            };
        });
    }
}
