package com.iafenvoy.jupiter.network.payload;

import com.iafenvoy.jupiter.Jupiter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigRequestPayload(Identifier id) implements CustomPayload {
    public static final Id<ConfigRequestPayload> ID = new Id<>(Identifier.of(Jupiter.MOD_ID, "config_request"));
    public static final PacketCodec<PacketByteBuf, ConfigRequestPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeIdentifier(value.id), buf -> new ConfigRequestPayload(buf.readIdentifier()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
