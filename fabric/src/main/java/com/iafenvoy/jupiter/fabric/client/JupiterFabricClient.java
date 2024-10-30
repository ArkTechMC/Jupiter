package com.iafenvoy.jupiter.fabric.client;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.api.JupiterConfigEntry;
import com.iafenvoy.jupiter.fabric.FabricEntryPointLoader;
import com.iafenvoy.jupiter.fabric.reloader.ClientConfigReloader;
import com.iafenvoy.jupiter.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public final class JupiterFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Jupiter.processClient();
        for (JupiterConfigEntry entry : FabricEntryPointLoader.INSTANCE.getEntries()) {
            try {
                entry.initializeClientConfig(ConfigManager.getInstance());
            } catch (Exception e) {
                Jupiter.LOGGER.error("Error running Jupiter config client entry.", e);
            }
        }
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ClientConfigReloader());
    }
}
