package com.iafenvoy.jupiter.malilib.config;

import com.iafenvoy.jupiter.config.AbstractConfigContainer;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager implements IConfigManager {
    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Map<Identifier, IConfigHandler> configHandlers = new HashMap<>();

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerConfigHandler(Identifier id, IConfigHandler handler) {
        this.configHandlers.put(id, handler);
        handler.init();
        handler.load();
    }

    public void registerConfigHandler(AbstractConfigContainer configContainer) {
        this.registerConfigHandler(configContainer.getConfigId(), configContainer);
    }

    @Override
    public void onConfigsChanged(Identifier id) {
        IConfigHandler handler = this.configHandlers.get(id);
        if (handler != null)
            handler.onConfigsChanged();
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void saveAllConfigs() {
        for (IConfigHandler handler : this.configHandlers.values())
            handler.save();
    }
}
