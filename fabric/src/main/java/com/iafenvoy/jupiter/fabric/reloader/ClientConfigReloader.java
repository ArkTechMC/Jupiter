package com.iafenvoy.jupiter.fabric.reloader;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.malilib.config.ConfigManager;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;

public class ClientConfigReloader extends ConfigManager implements IdentifiableResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return new Identifier(Jupiter.MOD_ID, "client_config_reload");
    }
}