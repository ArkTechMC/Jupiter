package com.iafenvoy.jupiter.network.neoforge;

import com.iafenvoy.jupiter.neoforge.network.ClientNetworkContainer;
import com.iafenvoy.jupiter.network.ClientNetworkHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ClientNetworkHelperImpl {
    public static void sendToServer(Identifier id, PacketByteBuf buf) {
        ClientNetworkContainer.sendToServer(id, buf);
    }

    public static void registerReceiver(Identifier id, ClientNetworkHelper.Handler handler) {
        ClientNetworkContainer.registerReceiver(id, handler);
    }
}
