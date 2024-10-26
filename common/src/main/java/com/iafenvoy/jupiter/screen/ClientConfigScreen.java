package com.iafenvoy.jupiter.screen;

import com.iafenvoy.jupiter.container.AbstractConfigContainer;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class ClientConfigScreen extends AbstractConfigScreen {
    public ClientConfigScreen(Screen parent, AbstractConfigContainer configContainer) {
        super(parent, configContainer);
    }

    @Override
    protected String getCurrentEditText() {
        return StringUtils.translate("jupiter.screen.current_modifying_client");
    }
}
