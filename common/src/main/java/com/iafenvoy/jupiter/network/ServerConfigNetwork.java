package com.iafenvoy.jupiter.network;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.iafenvoy.jupiter.config.AbstractConfigContainer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ServerConfigNetwork {
    public static void init() {
        ServerNetworkHelper.registerReceiver(NetworkConstants.CONFIG_REQUEST_C2S, (server, player, buf) -> {
            Identifier id = buf.readIdentifier();
            Jupiter.LOGGER.info("Player {} request to open config {}", player.getName().getString(), id);
            PacketByteBuf b = ByteBufUtil.create().writeIdentifier(id);
            if (ServerConfigManager.checkPermission(id, server, player)) {
                b.writeBoolean(true);
                AbstractConfigContainer data = ServerConfigManager.getConfig(id);
                assert data != null;
                b.writeString(data.serialize());
            } else b.writeBoolean(false);
            return () -> ServerNetworkHelper.sendToPlayer(player, NetworkConstants.CONFIG_SYNC_S2C, b);
        });
        ServerNetworkHelper.registerReceiver(NetworkConstants.CONFIG_SYNC_C2S, (server, player, buf) -> {
            Identifier id = buf.readIdentifier();
            Jupiter.LOGGER.info("Player {} request to change config {}", player.getName().getString(), id);
            String data = buf.readString();
            return () -> {
                if (ServerConfigManager.checkPermission(id, server, player)) {
                    AbstractConfigContainer container = ServerConfigManager.getConfig(id);
                    if (container != null) {
                        Jupiter.LOGGER.info(data);
                        container.deserializeWithoutCheck(data);
                        container.onConfigsChanged();
                        Jupiter.LOGGER.info("Player {} changed config {}", player.getName().getString(), id);
                    }
                } else
                    ServerNetworkHelper.sendToPlayer(player, NetworkConstants.CONFIG_ERROR_S2C, ByteBufUtil.create());
            };
        });
    }
}
