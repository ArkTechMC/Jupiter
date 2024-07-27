package com.iafenvoy.jupiter.fabric.compat;

import com.iafenvoy.jupiter.screen.ConfigSelectScreen;
import com.iafenvoy.jupiter.test.TestConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.text.Text;

public class ModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigSelectScreen<>(Text.translatable("jupiter.test_config"), parent, TestConfig.INSTANCE, null);
    }
}
