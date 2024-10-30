package com.iafenvoy.jupiter.render.screen;

import com.iafenvoy.jupiter.config.container.AbstractConfigContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;

@Environment(EnvType.CLIENT)
public class ClientConfigScreen extends AbstractConfigScreen {
    public ClientConfigScreen(Screen parent, AbstractConfigContainer configContainer) {
        super(parent, configContainer);
    }

    @Override
    protected String getCurrentEditText() {
        return I18n.translate("jupiter.screen.current_modifying_client");
    }
}
