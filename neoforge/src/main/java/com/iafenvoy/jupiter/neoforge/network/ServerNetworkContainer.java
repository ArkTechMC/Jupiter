package com.iafenvoy.jupiter.neoforge.network;

import com.iafenvoy.jupiter.network.ServerNetworkHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.HashMap;
import java.util.Map;

public class ServerNetworkContainer {
    private static final Map<Identifier, ServerNetworkHelper.Handler> HANDLERS = new HashMap<>();

    public static void registerReceiver(Identifier id, ServerNetworkHelper.Handler handler) {
        HANDLERS.put(id, handler);
    }

    public static boolean onReceive(Identifier id, PacketByteBuf buf, PlayPayloadContext context) {
        ServerNetworkHelper.Handler handler = HANDLERS.get(id);
        if (handler == null) return false;
        if (context.player().isEmpty()) return false;
        PlayerEntity player = context.player().get();
        MinecraftServer server = player.getServer();
        Runnable runnable = handler.handle(server, (ServerPlayerEntity) player, buf);
        if (server != null && runnable != null) server.execute(runnable);
        return true;
    }

    public static void sendToPlayer(ServerPlayerEntity player, Identifier id, PacketByteBuf buf) {
        PacketDistributor.PLAYER.with(player).send(new PacketByteBufS2C(id, buf));
    }
}
