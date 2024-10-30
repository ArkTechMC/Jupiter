package com.iafenvoy.jupiter.fabric.compat;

import com.iafenvoy.jupiter.render.screen.ConfigScreen;
import com.iafenvoy.jupiter.test.TestConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent, TestConfig.INSTANCE) {
            @Override
            protected String getCurrentEditText() {
                return "";
            }
        };
    }
}
