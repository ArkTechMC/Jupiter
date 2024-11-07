package com.iafenvoy.jupiter.neoforge.network;

import com.iafenvoy.jupiter.Jupiter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class PacketByteBufS2C implements CustomPayload {
    public static final Identifier ID = new Identifier(Jupiter.MOD_ID, "packet_byte_buf_s2c");
    private final Identifier id;
    private final PacketByteBuf buf;

    public PacketByteBufS2C(Identifier id, PacketByteBuf buf) {
        this.id = id;
        this.buf = buf;
    }

    public static void encode(PacketByteBufS2C message, PacketByteBuf buf) {
        buf.writeIdentifier(message.id).writeBytes(message.buf);
    }

    public static PacketByteBufS2C decode(PacketByteBuf buf) {
        return new PacketByteBufS2C(buf.readIdentifier(), buf);
    }

    public static void handle(PacketByteBufS2C message, PlayPayloadContext ctx) {
        ClientNetworkContainer.onReceive(message.id, message.buf, ctx);
    }

    @Override
    public void write(PacketByteBuf buf) {
        encode(this, buf);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
