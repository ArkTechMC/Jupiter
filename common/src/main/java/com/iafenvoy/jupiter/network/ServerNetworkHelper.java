package com.iafenvoy.jupiter.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ServerNetworkHelper {
    @ExpectPlatform
    public static void sendToPlayer(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    @ExpectPlatform
    public static void registerReceiver(Identifier id, Handler handler) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    public interface Handler {
        Runnable handle(MinecraftServer server, ServerPlayerEntity player, PacketByteBuf buf);
    }
}
