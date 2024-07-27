package com.iafenvoy.jupiter.forge.network;

import com.iafenvoy.jupiter.forge.JupiterForge;
import com.iafenvoy.jupiter.network.ServerNetworkHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class ServerNetworkContainer {
    private static final Map<Identifier, ServerNetworkHelper.Handler> HANDLERS = new HashMap<>();

    public static void registerReceiver(Identifier id, ServerNetworkHelper.Handler handler) {
        HANDLERS.put(id, handler);
    }

    public static boolean onReceive(Identifier id, PacketByteBuf buf, NetworkEvent.Context context) {
        ServerNetworkHelper.Handler handler = HANDLERS.get(id);
        if (handler == null) return false;
        if (context.getSender() == null) return false;
        Runnable runnable = handler.handle(context.getSender().server, context.getSender(), buf);
        if (runnable != null) context.enqueueWork(runnable);
        return true;
    }

    public static void sendToPlayer(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
        JupiterForge.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketByteBufS2C(id, buf));
    }
}
