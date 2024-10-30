package com.iafenvoy.jupiter;

import com.iafenvoy.jupiter.config.container.AbstractConfigContainer;
import com.iafenvoy.jupiter.interfaces.IConfigHandler;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager implements SynchronousResourceReloader {
    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Map<Identifier, IConfigHandler> configHandlers = new HashMap<>();

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public void registerConfigHandler(Identifier id, IConfigHandler handler) {
        this.configHandlers.put(id, handler);
        handler.init();
        handler.load();
    }

    public void registerConfigHandler(AbstractConfigContainer configContainer) {
        this.registerConfigHandler(configContainer.getConfigId(), configContainer);
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
