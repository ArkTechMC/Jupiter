package com.iafenvoy.jupiter.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ServerNetworkHelper {
    @ExpectPlatform
    public static void sendToPlayer(ServerPlayerEntity player, CustomPayload payload) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    @ExpectPlatform
    public static <T extends CustomPayload> void registerPayloadType(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    @ExpectPlatform
    public static <T extends CustomPayload> void registerReceiver(CustomPayload.Id<T> id, ServerNetworkHelper.Handler<T> handler) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    public interface Handler<T extends CustomPayload> {
        Runnable handle(MinecraftServer server, ServerPlayerEntity player, T payload);
    }
}
