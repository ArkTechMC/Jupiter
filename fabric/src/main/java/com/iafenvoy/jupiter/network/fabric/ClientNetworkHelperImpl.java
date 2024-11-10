package com.iafenvoy.jupiter.network.fabric;

import com.iafenvoy.jupiter.network.ClientNetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;

public class ClientNetworkHelperImpl {
    public static void sendToServer(CustomPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    public static <T extends CustomPayload> void registerReceiver(CustomPayload.Id<T> id, ClientNetworkHelper.Handler<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (payload, ctx) -> {
            Runnable runnable = handler.handle(ctx.client(), payload);
            if (runnable != null) ctx.client().execute(runnable);
        });
    }
}
