package com.iafenvoy.jupiter.network.fabric;

import com.iafenvoy.jupiter.network.ServerNetworkHelper;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerNetworkHelperImpl {
    public static void sendToPlayer(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static <T extends CustomPayload> void registerPayloadType(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec) {
        PayloadTypeRegistry.playC2S().register(id, codec);
        PayloadTypeRegistry.playS2C().register(id, codec);
    }

    public static <T extends CustomPayload> void registerReceiver(CustomPayload.Id<T> id, ServerNetworkHelper.Handler<T> handler) {
        ServerPlayNetworking.registerGlobalReceiver(id, (payload, ctx) -> {
            Runnable runnable = handler.handle(ctx.server(), ctx.player(), payload);
            if (runnable != null) ctx.server().execute(runnable);
        });
    }
}
