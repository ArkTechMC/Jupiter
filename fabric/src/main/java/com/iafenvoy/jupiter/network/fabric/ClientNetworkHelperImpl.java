package com.iafenvoy.jupiter.network.fabric;

import com.iafenvoy.jupiter.network.ClientNetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ClientNetworkHelperImpl {
    public static void sendToServer(Identifier id, PacketByteBuf buf) {
        ClientPlayNetworking.send(id, buf);
    }

    public static void registerReceiver(Identifier id, ClientNetworkHelper.Handler handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handler1, buf, responseSender) -> {
            Runnable runnable = handler.handle(client, buf);
            if (runnable != null) client.execute(runnable);
        });
    }
}
