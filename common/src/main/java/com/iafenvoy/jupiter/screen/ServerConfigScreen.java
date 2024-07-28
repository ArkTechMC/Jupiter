package com.iafenvoy.jupiter.screen;

import com.iafenvoy.jupiter.config.AbstractConfigContainer;
import com.iafenvoy.jupiter.config.FakeConfigContainer;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class ServerConfigScreen extends AbstractConfigScreen {
    public ServerConfigScreen(Screen parent, AbstractConfigContainer configContainer) {
        super(parent, configContainer);
    }

    @Override
    protected String getCurrentEditText() {
        if (this.configContainer instanceof FakeConfigContainer)
            return StringUtils.translate("jupiter.screen.current_modifying_dedicate_server");
        else
            return StringUtils.translate("jupiter.screen.current_modifying_local_server");
    }
}
