package com.iafenvoy.jupiter.network.payload;

import com.iafenvoy.jupiter.Jupiter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigErrorPayload() implements CustomPayload {
    public static final Id<ConfigErrorPayload> ID = new Id<>(new Identifier(Jupiter.MOD_ID, "config_error"));
    public static final PacketCodec<PacketByteBuf, ConfigErrorPayload> CODEC = PacketCodec.unit(new ConfigErrorPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
