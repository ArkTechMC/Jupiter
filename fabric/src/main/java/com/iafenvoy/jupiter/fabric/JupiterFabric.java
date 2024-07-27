package com.iafenvoy.jupiter.fabric;

import com.iafenvoy.jupiter.Jupiter;
import net.fabricmc.api.ModInitializer;

public final class JupiterFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Jupiter.init();
        Jupiter.process();
    }
}
