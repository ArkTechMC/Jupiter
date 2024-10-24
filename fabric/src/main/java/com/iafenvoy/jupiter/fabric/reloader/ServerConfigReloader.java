package com.iafenvoy.jupiter.fabric.reloader;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.ServerConfigManager;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;

public class ServerConfigReloader extends ServerConfigManager implements IdentifiableResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return new Identifier(Jupiter.MOD_ID, "server_config_reload");
    }
}
