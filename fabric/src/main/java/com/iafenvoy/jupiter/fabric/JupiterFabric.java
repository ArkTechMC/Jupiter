package com.iafenvoy.jupiter.fabric;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.api.JupiterConfigEntry;
import com.iafenvoy.jupiter.fabric.reloader.ServerConfigReloader;
import com.iafenvoy.jupiter.malilib.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public final class JupiterFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Jupiter.init();
        Jupiter.process();
        for (JupiterConfigEntry entry : FabricEntryPointLoader.INSTANCE.getEntries()) {
            try {
                entry.initializeCommonConfig(ConfigManager.getInstance());
            } catch (Exception e) {
                Jupiter.LOGGER.error("Error running Jupiter config entry.", e);
            }
        }
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ServerConfigReloader());
    }
}
