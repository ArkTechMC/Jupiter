package com.iafenvoy.jupiter.network.neoforge;

import com.iafenvoy.jupiter.network.ServerNetworkHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ServerNetworkHelperImpl {
    public static final Map<CustomPayload.Id<CustomPayload>, PacketCodec<PacketByteBuf, CustomPayload>> TYPES = new HashMap<>();
    public static final Map<CustomPayload.Id<CustomPayload>, ServerNetworkHelper.Handler<CustomPayload>> RECEIVERS = new HashMap<>();

    public static void sendToPlayer(ServerPlayerEntity player, CustomPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @SuppressWarnings("unchecked")
    public static <T extends CustomPayload> void registerPayloadType(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec) {
        TYPES.put((CustomPayload.Id<CustomPayload>) id, (PacketCodec<PacketByteBuf, CustomPayload>) codec);
    }

    @SuppressWarnings("unchecked")
    public static <T extends CustomPayload> void registerReceiver(CustomPayload.Id<T> id, ServerNetworkHelper.Handler<T> handler) {
        RECEIVERS.put((CustomPayload.Id<CustomPayload>) id, (ServerNetworkHelper.Handler<CustomPayload>) handler);
    }

    public static void handleData(CustomPayload payload, IPayloadContext ctx) {
        RECEIVERS.entrySet().stream().filter(x -> x.getKey().id().equals(payload.getId().id())).map(e -> e.getValue().handle(ctx.player().getServer(), (ServerPlayerEntity) ctx.player(), payload)).filter(Objects::nonNull).forEach(Runnable::run);
    }
}
