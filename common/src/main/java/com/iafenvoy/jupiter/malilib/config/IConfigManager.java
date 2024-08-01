package com.iafenvoy.jupiter.malilib.config;

import net.minecraft.util.Identifier;

public interface IConfigManager {
    static IConfigManager getInstance() {
        return ConfigManager.getInstance();
    }

    /**
     * Registers a config handler
     */
    void registerConfigHandler(Identifier id, IConfigHandler handler);

    /**
     * Can be called to save and reload the configs for the given mod.
     */
    void onConfigsChanged(Identifier id);
}
