package com.iafenvoy.jupiter.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;

public class ByteBufUtil {
    public static PacketByteBuf create() {
        return new PacketByteBuf(Unpooled.buffer());
    }
}
