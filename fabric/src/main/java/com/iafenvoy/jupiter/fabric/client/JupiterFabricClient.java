package com.iafenvoy.jupiter.fabric.client;

import com.iafenvoy.jupiter.Jupiter;
import net.fabricmc.api.ClientModInitializer;

public final class JupiterFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Jupiter.processClient();
    }
}
