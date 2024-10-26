package com.iafenvoy.jupiter.config.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ScreenBase extends Screen {
    protected final List<ClickableWidget> widgets = new ArrayList<>();

    protected ScreenBase(Text title) {
        super(title);
    }
}
