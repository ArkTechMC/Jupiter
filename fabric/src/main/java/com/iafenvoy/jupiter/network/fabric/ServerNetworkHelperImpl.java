package com.iafenvoy.jupiter.network.fabric;

import com.iafenvoy.jupiter.network.ServerNetworkHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ServerNetworkHelperImpl {
    public static void sendToPlayer(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
        ServerPlayNetworking.send(player, id, buf);
    }

    public static void registerReceiver(Identifier id, ServerNetworkHelper.Handler handler) {
        ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handler1, buf, responseSender) -> {
            Runnable runnable = handler.handle(server, player, buf);
            if (runnable != null) server.execute(runnable);
        });
    }
}
