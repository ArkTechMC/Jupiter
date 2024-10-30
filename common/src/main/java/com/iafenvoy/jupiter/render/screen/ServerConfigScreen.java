package com.iafenvoy.jupiter.render.screen;

import com.iafenvoy.jupiter.config.container.AbstractConfigContainer;
import com.iafenvoy.jupiter.config.container.FakeConfigContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;

@Environment(EnvType.CLIENT)
public class ServerConfigScreen extends AbstractConfigScreen{
    public ServerConfigScreen(Screen parent, AbstractConfigContainer configContainer) {
        super(parent, configContainer);
    }

    @Override
    protected String getCurrentEditText() {
        if (this.configContainer instanceof FakeConfigContainer)
            return I18n.translate("jupiter.screen.current_modifying_dedicate_server");
        else
            return I18n.translate("jupiter.screen.current_modifying_local_server");
    }
}
