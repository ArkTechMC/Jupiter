package com.iafenvoy.jupiter.network.neoforge;

import com.iafenvoy.jupiter.neoforge.network.ServerNetworkContainer;
import com.iafenvoy.jupiter.network.ServerNetworkHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ServerNetworkHelperImpl {
    public static void sendToPlayer(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
        ServerNetworkContainer.sendToPlayer(player, id, buf);
    }

    public static void registerReceiver(Identifier id, ServerNetworkHelper.Handler handler) {
        ServerNetworkContainer.registerReceiver(id, handler);
    }
}
