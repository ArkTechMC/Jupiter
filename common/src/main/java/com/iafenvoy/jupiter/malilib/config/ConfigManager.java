package com.iafenvoy.jupiter.malilib.config;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.iafenvoy.jupiter.config.container.AbstractConfigContainer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager implements IConfigManager, SynchronousResourceReloader {
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
            handler.save();
    }

    public void registerServerConfig(AbstractConfigContainer data, ServerConfigManager.PermissionChecker checker) {
        ServerConfigManager.registerServerConfig(data, checker);
    }

    @Override
    public void reload(ResourceManager manager) {
        configHandlers.values().forEach(IConfigHandler::load);
        Jupiter.LOGGER.info("Successfully reload {} common config(s).", configHandlers.size());
    }
}
