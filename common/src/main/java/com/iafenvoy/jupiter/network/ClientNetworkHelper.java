package com.iafenvoy.jupiter.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.CustomPayload;

@Environment(EnvType.CLIENT)
public class ClientNetworkHelper {
    @ExpectPlatform
    public static void sendToServer(CustomPayload payload) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    @ExpectPlatform
    public static <T extends CustomPayload> void registerReceiver(CustomPayload.Id<T> id, ClientNetworkHelper.Handler<T> handler) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    public interface Handler<T extends CustomPayload> {
        Runnable handle(MinecraftClient client, T payload);
    }
}
