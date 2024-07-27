package com.iafenvoy.jupiter.forge.network;

import com.iafenvoy.jupiter.network.ByteBufUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketByteBufS2C {
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
        return new PacketByteBufS2C(buf.readIdentifier(), new PacketByteBuf(buf.readBytes(ByteBufUtil.create())));
    }

    public static void handle(PacketByteBufS2C message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (ClientNetworkContainer.onReceive(message.id, message.buf, context))
            context.setPacketHandled(true);
    }
}
