package com.iafenvoy.jupiter.network.neoforge;

import com.iafenvoy.jupiter.network.ClientNetworkHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.CustomPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClientNetworkHelperImpl {
    public static final Map<CustomPayload.Id<CustomPayload>, ClientNetworkHelper.Handler<CustomPayload>> RECEIVERS = new HashMap<>();

    public static void sendToServer(CustomPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    @SuppressWarnings("unchecked")
    public static <T extends CustomPayload> void registerReceiver(CustomPayload.Id<T> id, ClientNetworkHelper.Handler<T> handler) {
        RECEIVERS.put((CustomPayload.Id<CustomPayload>) id, (ClientNetworkHelper.Handler<CustomPayload>) handler);
    }

    public static void handleData(CustomPayload payload, IPayloadContext ctx) {
        RECEIVERS.entrySet().stream().filter(x -> x.getKey().id().equals(payload.getId().id())).map(e -> e.getValue().handle(MinecraftClient.getInstance(), payload)).filter(Objects::nonNull).forEach(Runnable::run);
    }
}
