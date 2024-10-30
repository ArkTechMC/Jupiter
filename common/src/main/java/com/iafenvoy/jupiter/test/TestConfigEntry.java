package com.iafenvoy.jupiter.test;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.iafenvoy.jupiter.api.JupiterConfig;
import com.iafenvoy.jupiter.api.JupiterConfigEntry;
import com.iafenvoy.jupiter.ConfigManager;
import net.minecraft.util.Identifier;

@JupiterConfig
public class TestConfigEntry implements JupiterConfigEntry {
    @Override
    public Identifier getId() {
        return new Identifier(Jupiter.MOD_ID, "jupiter");
    }

    @Override
    public void initializeCommonConfig(ConfigManager manager) {
        manager.registerConfigHandler(TestConfig.INSTANCE);
        manager.registerServerConfig(TestConfig.INSTANCE, ServerConfigManager.PermissionChecker.IS_OPERATOR);
    }
}
