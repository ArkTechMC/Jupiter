package com.iafenvoy.jupiter.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ClientConfigNetwork {
    private static final Map<Identifier, Consumer<String>> CALLBACKS = new HashMap<>();

    // will pass null to string if not allowed
    public static void startConfigSync(Identifier id, Consumer<String> callback) {
        CALLBACKS.put(id, callback);
        ClientNetworkHelper.sendToServer(NetworkConstants.CONFIG_REQUEST_C2S, ByteBufUtil.create().writeIdentifier(id));
    }

    public static void init() {
        ClientNetworkHelper.registerReceiver(NetworkConstants.CONFIG_SYNC_S2C, (client, buf) -> {
            Identifier id = buf.readIdentifier();
            boolean allow = buf.readBoolean();
            Consumer<String> callback = CALLBACKS.get(id);
            if (callback == null) return null;
            if (allow) {
                String data = buf.readString();
                return () -> callback.accept(data);
            } else
                return () -> callback.accept(null);
        });
        ClientNetworkHelper.registerReceiver(NetworkConstants.CONFIG_ERROR_S2C, (client, buf) -> () -> client.getToastManager().add(new SystemToast(SystemToast.Type.WORLD_ACCESS_FAILURE, Text.translatable("jupiter.toast.upload_config_error_title"), Text.translatable("jupiter.toast.upload_config_error_content"))));
    }
}
