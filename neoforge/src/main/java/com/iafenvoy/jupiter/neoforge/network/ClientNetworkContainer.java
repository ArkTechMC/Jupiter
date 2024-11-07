package com.iafenvoy.jupiter.neoforge.network;

import com.iafenvoy.jupiter.network.ClientNetworkHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.HashMap;
import java.util.Map;

public class ClientNetworkContainer {
    private static final Map<Identifier, ClientNetworkHelper.Handler> HANDLERS = new HashMap<>();

    public static void registerReceiver(Identifier id, ClientNetworkHelper.Handler handler) {
        HANDLERS.put(id, handler);
    }

    public static boolean onReceive(Identifier id, PacketByteBuf buf, PlayPayloadContext context) {
        ClientNetworkHelper.Handler handler = HANDLERS.get(id);
        if (handler == null) return false;
        Runnable runnable = handler.handle(MinecraftClient.getInstance(), buf);
        if (runnable != null) MinecraftClient.getInstance().execute(runnable);
        return true;
    }

    public static void sendToServer(Identifier id, PacketByteBuf buf) {
        PacketDistributor.SERVER.noArg().send(new PacketByteBufC2S(id, buf));
    }
}
