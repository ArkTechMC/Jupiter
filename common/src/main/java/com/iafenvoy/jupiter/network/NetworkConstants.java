package com.iafenvoy.jupiter.network;

import com.iafenvoy.jupiter.Jupiter;
import net.minecraft.util.Identifier;

public class NetworkConstants {
    public static final Identifier CONFIG_SYNC_C2S = new Identifier(Jupiter.MOD_ID, "config_sync_c2s");
    public static final Identifier CONFIG_SYNC_S2C = new Identifier(Jupiter.MOD_ID, "config_sync_s2c");
    public static final Identifier CONFIG_REQUEST_C2S = new Identifier(Jupiter.MOD_ID, "config_request_c2s");
    public static final Identifier CONFIG_ERROR_S2C = new Identifier(Jupiter.MOD_ID, "config_error_s2c");
}
