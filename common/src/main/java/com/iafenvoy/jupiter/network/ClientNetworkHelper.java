package com.iafenvoy.jupiter.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientNetworkHelper {
    @ExpectPlatform
    public static void sendToServer(Identifier id, PacketByteBuf buf) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    @ExpectPlatform
    public static void registerReceiver(Identifier id, Handler handler) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    public interface Handler {
        Runnable handle(MinecraftClient client, PacketByteBuf buf);
    }
}
